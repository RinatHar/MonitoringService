package org.kharisov.in.servlets.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.dtos.in.ReadingDtoIn;
import org.kharisov.dtos.in.RefreshTokenDtoIn;
import org.kharisov.entities.ReadingRecord;
import org.kharisov.entities.ReadingType;
import org.kharisov.entities.User;
import org.kharisov.enums.Role;
import org.kharisov.mappers.ReadingMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.services.interfaces.ReadingService;
import org.kharisov.utils.AuthUtils;
import org.kharisov.utils.DtoUtils;
import org.kharisov.utils.ResponseUtils;
import org.kharisov.validators.DtosInValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
