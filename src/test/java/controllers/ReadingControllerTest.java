package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kharisov.configs.AppConfig;
import org.kharisov.dtos.ReadingDto;
import org.kharisov.entities.*;
import org.kharisov.in.controllers.ReadingController;
import org.kharisov.services.interfaces.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class ReadingControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private ReadingService readingService;

    @Mock
    private ReadingTypeService readingTypeService;

    @InjectMocks
    private ReadingController readingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        UserRecord user = new UserRecord(null,null, null, null);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("Тест получения текущего показания по типу без ролей")
    public void getCurrentReadingTestWithoutRoles() throws Exception {
        UserRecord user = new UserRecord(null,null, null, null);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/readings/current?type=testType"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Тест получения текущего показания по типу")
    public void getCurrentReadingTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(readingController)
                .build();

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/readings/current"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест получения показаний по месяцам")
    public void getReadingsByMonthTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(readingController)
                .build();

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/readings/month?year=abcd&month=2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Incorrect year or month. They should be numbers.\""));
    }

    @Test
    @DisplayName("Тест получения показаний по месяцам с недействительным месяцем")
    public void getReadingsByMonthTestInvalidMonth() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        mockMvc.perform(get("/api/v1/readings/month?year=2024&month=gdf"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Incorrect year or month. They should be numbers.\""));
    }

    @Test
    @DisplayName("Тест получения истории показаний")
    public void getHistoryTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(readingController)
                .build();

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
        mockMvc = MockMvcBuilders.standaloneSetup(readingController)
                .build();

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        ReadingDto readingDto = new ReadingDto("testType", "value");

        mockMvc.perform(post("/api/v1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(readingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Data entered incorrectly\""));
    }
}
