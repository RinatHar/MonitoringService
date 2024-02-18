package org.kharisov.in.servlets.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.dtos.in.RefreshTokenDtoIn;
import org.kharisov.domains.User;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.*;

import java.io.IOException;
import java.util.*;

/**
 * Сервлет TokenRefreshServlet обрабатывает запросы по пути "/api/v1/refresh-token".
 * Этот сервлет наследуется от HttpServlet и предоставляет функциональность обновления токена.
 *
 * <p>Сервлет использует следующие сервисы:</p>
 * <ul>
 *   <li>AuthService: Сервис аутентификации.</li>
 * </ul>
 *
 * @see HttpServlet
 */
@WebServlet("/api/v1/refresh-token")
public class TokenRefreshServlet extends HttpServlet {

    private AuthService authService;

    /**
     * Инициализирует сервлет и получает сервис аутентификации из контекста сервлета.
     */
    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    public TokenRefreshServlet() {
    }

    /**
     * Обрабатывает POST-запросы для обновления токена.
     * Проверяет валидность данных обновления токена, преобразует DTO в сущность пользователя и пытается обновить токен.
     * Если обновление токена успешно, отправляет ответ с новыми JWT. В противном случае отправляет ошибку.
     *
     * @param req  объект HttpServletRequest, который содержит запрос клиента
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RefreshTokenDtoIn refreshTokenDto = DtoUtils.parseDtoFromRequest(req, RefreshTokenDtoIn.class);
        if (refreshTokenDto == null || refreshTokenDto.getRefreshToken().isEmpty()) {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }

        String refreshToken = refreshTokenDto.getRefreshToken();
        String accountNum = AuthUtils.getSubjectFromJwt(refreshToken);
        Optional<User> optionalUser = authService.getUserByAccountNum(accountNum);
        if (optionalUser.isEmpty()) {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
            return;
        }
        User user = optionalUser.get();

        Map<String, String> tokens = AuthUtils.createJwtForUser(user);
        ResponseUtils.sendJwtResponse(resp, tokens);
    }
}
