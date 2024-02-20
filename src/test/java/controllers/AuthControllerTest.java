package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.kharisov.configs.AppConfig;
import org.kharisov.dtos.*;
import org.kharisov.entities.UserRecord;
import org.kharisov.in.controllers.AuthController;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.JwtUtils;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class AuthControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loginTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();

        UserDto userDto = new UserDto("0000000000000000", "testPassword");

        when(authService.logIn(any(UserRecord.class)))
                .thenReturn(new UserRecord(null, null, null, null));
        when(jwtUtils.generateTokens(new UserRecord(null, null, null, null)))
                .thenReturn(new HashMap<>());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTestInvalidDto() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        UserDto userDto = new UserDto("accountNum", "pass");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Data entered incorrectly\""));
    }

    @Test
    public void registerTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();

        UserDto userDto = new UserDto("0000000000000000", "testPassword");

        when(authService.addUser(any(UserRecord.class)))
                .thenReturn(Optional.of(new UserRecord(null, null, null, null)));
        when(jwtUtils.generateTokens(any(UserRecord.class)))
                .thenReturn(new HashMap<>());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerTestInvalidDto() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        UserDto userDto = new UserDto("accountNum", "pass");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Data entered incorrectly\""));
    }

    @Test
    public void refreshTokenTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000");

        when(authService.getUserById(anyLong()))
                .thenReturn(Optional.of(new UserRecord(null, null, null, null)));
        when(jwtUtils.generateTokens(any(UserRecord.class)))
                .thenReturn(new HashMap<>());

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(refreshTokenDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void refreshTokenTestInvalidParam() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("000000");

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(refreshTokenDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Data entered incorrectly\""));
    }
}
