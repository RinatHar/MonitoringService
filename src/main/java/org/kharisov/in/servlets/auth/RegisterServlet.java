package org.kharisov.in.servlets.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.dtos.in.UserDtoIn;
import org.kharisov.domains.User;
import org.kharisov.mappers.UserMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.*;
import org.kharisov.validators.DtoInValidator;

import java.io.IOException;
import java.util.*;

/**
 * Сервлет RegisterServlet обрабатывает запросы по пути "/api/v1/register".
 * Этот сервлет наследуется от HttpServlet и предоставляет функциональность регистрации пользователя.
 *
 * <p>Сервлет использует следующие сервисы:</p>
 * <ul>
 *   <li>AuthService: Сервис аутентификации.</li>
 * </ul>
 *
 * @see HttpServlet
 */
@WebServlet("/api/v1/register")
public class RegisterServlet extends HttpServlet {

    private AuthService authService;

    /**
     * Инициализирует сервлет и получает сервис аутентификации из контекста сервлета.
     */
    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    public RegisterServlet() {
    }

    /**
     * Обрабатывает POST-запросы для регистрации пользователя.
     * Проверяет валидность данных пользователя, преобразует DTO в сущность пользователя и пытается добавить пользователя.
     * Если пользователь успешно добавлен, отправляет ответ с JWT. В противном случае отправляет ошибку.
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
        Optional<User> optionalUser = authService.addUser(user);
        if (optionalUser.isPresent()) {
            Map<String, String> tokens = AuthUtils.createJwtForUser(user);
            ResponseUtils.sendJwtResponse(resp, tokens);
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_CONFLICT, "User already exists");
        }
    }

}