package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.kharisov.MainApplication;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.configs.ConnectionPool;
import org.kharisov.dtos.*;
import org.kharisov.entities.ReadingTypeRecord;
import org.kharisov.enums.Role;
import org.kharisov.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainApplication.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
public class ReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReadingService readingService;

    @MockBean
    private ReadingTypeService readingTypeService;

    @MockBean
    private ConnectionPool connectionPool;

    private void authenticateWithRole(Role role) {
        UserRecord user = new UserRecord(null, null, null, null);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("Тест получения текущего показания по типу без ролей")
    public void getCurrentReadingTestWithoutRoles() throws Exception {
        mockMvc.perform(get("/api/v1/readings/current?type=testType"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Тест получения текущего показания по типу")
    public void getCurrentReadingTest() throws Exception {
        authenticateWithRole(Role.USER);
        ReadingDto readingDto = new ReadingDto("testType", "testValue");

        when(readingService.getCurrentReading(any(UserRecord.class), any(ReadingTypeRecord.class)))
                .thenReturn(readingDto);

        mockMvc.perform(get("/api/v1/readings/current?type=testType"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            {
                "type": "testType",
                "value": "testValue"
            }
            """));
    }

    @Test
    @DisplayName("Тест получения текущего показания с недействительным типом")
    public void getCurrentReadingTestInvalidType() throws Exception {
        authenticateWithRole(Role.USER);
        mockMvc.perform(get("/api/v1/readings/current"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест получения показаний по месяцам")
    public void getReadingsByMonthTest() throws Exception {
        authenticateWithRole(Role.USER);
        List<ReadingDto> readingList = Arrays.asList(
                new ReadingDto("type1", "value1"),
                new ReadingDto("type2", "value2"));

        when(readingService.getReadingsByMonth(any(UserRecord.class), anyInt(), anyInt()))
                .thenReturn(readingList);

        mockMvc.perform(get("/api/v1/readings/month?year=2024&month=2"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            [
                {
                    "type": "type1",
                    "value": "value1"
                },
                {
                    "type": "type2",
                    "value": "value2"
                }
            ]
            """));
    }

    @Test
    @DisplayName("Тест получения показаний по месяцам с недействительным годом")
    public void getReadingsByMonthTestInvalidYear() throws Exception {
        authenticateWithRole(Role.USER);
        mockMvc.perform(get("/api/v1/readings/month?year=abcd&month=2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter 'year' should be of type 'int'"));
    }

    @Test
    @DisplayName("Тест получения показаний по месяцам с недействительным месяцем")
    public void getReadingsByMonthTestInvalidMonth() throws Exception {
        authenticateWithRole(Role.USER);
        mockMvc.perform(get("/api/v1/readings/month?year=2024&month=gdf"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter 'month' should be of type 'int'"));
    }

    @Test
    @DisplayName("Тест получения истории показаний")
    public void getHistoryTest() throws Exception {
        authenticateWithRole(Role.USER);
        List<ReadingDto> readingList = Arrays.asList(
                new ReadingDto("type1", "value1"),
                new ReadingDto("type2", "value2"));

        when(readingService.getHistory(any(UserRecord.class)))
                .thenReturn(readingList);

        mockMvc.perform(get("/api/v1/readings/history"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            [
                {
                    "type": "type1",
                    "value": "value1"
                },
                {
                    "type": "type2",
                    "value": "value2"
                }
            ]
            """));
    }

    @Test
    @DisplayName("Тест подачи показания")
    public void addReadingTest() throws Exception {
        authenticateWithRole(Role.USER);
        ReadingDto readingDto = new ReadingDto("testType", "10");

        when(readingTypeService.getByName(anyString())).thenReturn(new ReadingTypeRecord(null, null));
        doNothing().when(readingService).addReading(any(UserRecord.class), any(ReadingTypeRecord.class), anyInt());

        mockMvc.perform(post("/api/v1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(readingDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("The reading has been sent successfully"));
    }

    @Test
    @DisplayName("Тест подачи показания с некорректным значением (не числовое)")
    public void addReadingTestInvalidDto() throws Exception {
        authenticateWithRole(Role.USER);
        ReadingDto readingDto = new ReadingDto("testType", "value");

        mockMvc.perform(post("/api/v1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(readingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Value is not an long"));
    }

    @Test
    @DisplayName("Получить все показания")
    @WithMockUser(roles = "ADMIN")
    public void getAllReadingsTest() throws Exception {
        List<ReadingDto> readingList = Arrays.asList(
                new ReadingDto("type1", "value1"),
                new ReadingDto("type2", "value2")
        );
        Map<String, List<ReadingDto>> allReadings = new HashMap<>();
        allReadings.put("test", readingList);

        when(readingService.getAllReadings()).thenReturn(allReadings);

        mockMvc.perform(get("/api/v1/readings"))
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
    @DisplayName("Добавление нового типа показания")
    @WithMockUser(roles = "ADMIN")
    public void addReadingTypeTestSuccess() throws Exception {
        ReadingTypeDto readingTypeDto = new ReadingTypeDto("testType");

        when(readingTypeService.addReadingType(any(ReadingTypeRecord.class))).thenReturn(any(ReadingTypeRecord.class));

        mockMvc.perform(post("/api/v1/readings/addReadingType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(readingTypeDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("The type of reading has been added successfully"));
    }

    @Test
    @DisplayName("Добавление некорректного типа показания")
    @WithMockUser(roles = "ADMIN")
    public void addReadingTypeTestFail() throws Exception {
        ReadingTypeDto readingTypeDto = new ReadingTypeDto();

        mockMvc.perform(post("/api/v1/readings/addReadingType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(readingTypeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name must not be null"));
    }
}
