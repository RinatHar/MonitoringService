package org.kharisov.in.servlets.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.dtos.in.RefreshTokenDtoIn;
import org.kharisov.entities.User;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/api/v1/refresh-token")
public class TokenRefreshServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    public TokenRefreshServlet() {
    }

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
