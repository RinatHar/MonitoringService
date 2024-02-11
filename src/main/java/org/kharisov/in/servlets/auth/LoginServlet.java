package org.kharisov.in.servlets.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.dtos.in.UserDtoIn;
import org.kharisov.domains.User;
import org.kharisov.enums.Role;
import org.kharisov.mappers.UserMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.*;
import org.kharisov.validators.DtoInValidator;

import java.io.IOException;
import java.util.*;

/**
 * Сервлет LoginServlet обрабатывает запросы по пути "/api/v1/login".
 * Этот сервлет наследуется от HttpServlet и предоставляет функциональность входа в систему.
 *
 * <p>Сервлет использует следующие сервисы:</p>
 * <ul>
 *   <li>AuthService: Сервис аутентификации.</li>
 * </ul>
 *
 * @see HttpServlet
 */
@WebServlet("/api/v1/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    /**
     * Инициализирует сервлет и получает сервис аутентификации из контекста сервлета.
     */
    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    public LoginServlet() {
    }

    /**
     * Обрабатывает POST-запросы для входа в систему.
     * Проверяет валидность данных пользователя, преобразует DTO в сущность пользователя и пытается войти в систему.
     * Если вход успешен, отправляет ответ с JWT. В противном случае отправляет ошибку.
     *
     * @param req  объект HttpServletRequest, который содержит запрос клиента
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDtoIn userDto = DtoUtils.parseDtoFromRequest(req, UserDtoIn.class);
        if (!DtoInValidator.isValid(userDto).isEmpty()) {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }
        User user = UserMapper.INSTANCE.toEntity(userDto);
        UserContextHolder.setUser(user);
        if (authService.isAdminByAccountNum(user.getAccountNum())) {
            user.setRole(Role.ADMIN);
        }
        boolean isSuccessLogin = authService.logIn(user.getAccountNum(), user.getPassword());
        if (isSuccessLogin) {
            Map<String, String> tokens = AuthUtils.createJwtForUser(user);
            ResponseUtils.sendJwtResponse(resp, tokens);
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
        }
    }
}
