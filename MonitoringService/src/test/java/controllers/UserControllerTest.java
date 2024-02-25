package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.kharisov.MainApplication;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.configs.ConnectionPool;
import org.kharisov.dtos.*;
import org.kharisov.enums.Role;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MainApplication.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private ConnectionPool connectionPool;

    @Test
    @DisplayName("Тест успешного входа в систему")
    public void loginTest() throws Exception {
        UserDto userDto = new UserDto("0000000000000000", "testPassword", "USER");

        when(authService.logIn(any(UserRecord.class)))
                .thenReturn(new UserRecord(null, null, null, null));
        when(jwtUtils.generateTokens(new UserRecord(null, null, null, null)))
                .thenReturn(new HashMap<>());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест входа в систему с недействительными данными")
    public void loginTestInvalidDto() throws Exception {
        UserDto userDto = new UserDto("5555", "password", "USER");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("AccountNum must be exactly 16 characters"));
    }

    @Test
    @DisplayName("Тест успешной регистрации")
    public void registerTest() throws Exception {
        UserDto userDto = new UserDto("0000000000000000", "testPassword", "USER");

        when(authService.addUser(any(UserRecord.class)))
                .thenReturn(new UserRecord(null, null, null, null));
        when(jwtUtils.generateTokens(any(UserRecord.class)))
                .thenReturn(new HashMap<>());

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Тест регистрации с недействительными данными")
    public void registerTestInvalidDto() throws Exception {
        UserDto userDto = new UserDto("0000000000000000", "pass", "ROLE");

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password must be between 8 and 128 characters"));
    }

    @Test
    @DisplayName("Тест обновления токена")
    public void refreshTokenTest() throws Exception {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000");

        when(authService.getUserById(anyLong()))
                .thenReturn(new UserRecord(null, null, null, null));
        when(jwtUtils.generateTokens(any(UserRecord.class)))
                .thenReturn(new HashMap<>());

        mockMvc.perform(post("/api/v1/users/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(refreshTokenDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест обновления токена с недействительным токеном обновления")
    public void refreshTokenTestInvalidParam() throws Exception {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto("000000");

        mockMvc.perform(post("/api/v1/users/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(refreshTokenDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Refresh token must be between 100 and 200 characters"));
    }

    @Test
    @DisplayName("Успешное изменение пользователя в администратора")
    @WithMockUser(roles = "ADMIN")
    public void makeAdminTestSuccess() throws Exception {
        UserDto userDto = new UserDto("0000000000000000", "testPassword", "ADMIN");

        doNothing().when(authService).changeUserRole(any(UserRecord.class), any(Role.class));

        mockMvc.perform(patch("/api/v1/users/" + userDto.getAccountNum())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("The user's role has been successfully updated"));
    }

    @Test
    @DisplayName("Создание администратора с недействительными данными")
    @WithMockUser(roles = "ADMIN")
    public void makeAdminTestInvalidDto() throws Exception {
        UserDto userDto = new UserDto("0000000000000000", "testPassword", "INVALID_ROLE");

        mockMvc.perform(patch("/api/v1/users/" + userDto.getAccountNum())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid role"));
    }
}
