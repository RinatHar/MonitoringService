package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.kharisov.configs.AppConfig;
import org.kharisov.dtos.*;
import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.in.controllers.AdminController;
import org.kharisov.services.interfaces.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class AdminControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private AuditService auditService;

    @Mock
    private ReadingService readingService;

    @Mock
    private ReadingTypeService readingTypeService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AdminController adminController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllAuditRecordsTestAsAdmin() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/admin/audit"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllAuditRecordsTestAsUser() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/admin/audit"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("\"Insufficient user rights\""));
    }

    @Test
    @WithMockUser
    public void getAllAuditRecordsTestWithoutRoles() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/admin/audit"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("\"Insufficient user rights\""));
    }

    @Test
    public void getAllAuditRecordsTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();

        Map<String, List<String>> allAudits = new HashMap<>();
        allAudits.put("test", Arrays.asList("record1", "record2"));

        when(auditService.getAllAuditRecords()).thenReturn(allAudits);

        mockMvc.perform(get("/api/v1/admin/audit"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            {
                "test": ["record1", "record2"]
            }
            """));
    }

    @Test
    public void getAllReadingsTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();

        List<ReadingDto> readingList = Arrays.asList(
                new ReadingDto("type1", "value1"),
                new ReadingDto("type2", "value2")
        );
        Map<String, List<ReadingDto>> allReadings = new HashMap<>();
        allReadings.put("test", readingList);

        when(readingService.getAllReadings()).thenReturn(allReadings);

        mockMvc.perform(get("/api/v1/admin/readings"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            {
                "test": [
                    {"type": "type1", "value": "value1"},
                    {"type": "type2", "value": "value2"}
                ]
            }
            """));
    }

    @Test
    public void makeAdminTestSuccess() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();

        UserDto userDto = new UserDto("0000000000000000", "testPassword");

        doNothing().when(authService).changeUserRole(any(UserRecord.class), any(Role.class));

        mockMvc.perform(post("/api/v1/admin/makeAdmin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("The user has successfully become an administrator"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void makeAdminTestInvalidDto() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        UserDto userDto = new UserDto("InvalidAccountNum", "testPassword");

        mockMvc.perform(post("/api/v1/admin/makeAdmin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"The user was not found\""));
    }

    @Test
    public void addReadingTypeTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();

        ReadingTypeDto readingTypeDto = new ReadingTypeDto("testType");

        when(readingTypeService.addReadingType(any(ReadingTypeRecord.class))).thenReturn(any(ReadingTypeRecord.class));

        mockMvc.perform(post("/api/v1/admin/addReadingType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(readingTypeDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("The type of reading has been added successfully"));
    }

}


