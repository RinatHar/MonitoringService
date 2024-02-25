package controllers;

import org.junit.jupiter.api.*;
import org.kharisov.MainApplication;
import org.kharisov.auditshared.services.interfaces.AuditService;
import org.kharisov.configs.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainApplication.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
public class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @MockBean
    private ConnectionPool connectionPool;

    @Test
    @DisplayName("Получить все записи аудита как администратор")
    @WithMockUser(roles = "ADMIN")
    public void getAllAuditRecordsTestAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/audit"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получить все записи аудита как пользователь (недостаточно прав)")
    @WithMockUser(roles = "USER")
    public void getAllAuditRecordsTestAsUser() throws Exception {
        mockMvc.perform(get("/api/v1/audit"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Insufficient user rights"));
    }

    @Test
    @DisplayName("Получить все записи аудита без ролей (недостаточно прав)")
    @WithMockUser
    public void getAllAuditRecordsTestWithoutRoles() throws Exception {
        mockMvc.perform(get("/api/v1/audit"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Insufficient user rights"));
    }

    @Test
    @DisplayName("Получить все записи аудита")
    @WithMockUser(roles = "ADMIN")
    public void getAllAuditRecordsTest() throws Exception {
        Map<String, List<String>> allAudits = new HashMap<>();
        allAudits.put("test", Arrays.asList("record1", "record2"));

        when(auditService.getAllAuditRecords()).thenReturn(allAudits);

        mockMvc.perform(get("/api/v1/audit"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            {
                "test": ["record1", "record2"]
            }
            """));
    }
}
