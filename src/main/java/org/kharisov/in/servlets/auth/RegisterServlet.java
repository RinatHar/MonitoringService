package org.kharisov.in.servlets.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.dtos.in.UserDtoIn;
import org.kharisov.entities.User;
import org.kharisov.mappers.UserMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.*;
import org.kharisov.validators.DtosInValidator;

import java.io.IOException;
import java.util.*;

@WebServlet("/api/v1/register")
public class RegisterServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    public RegisterServlet() {
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDtoIn userDto = DtoUtils.parseDtoFromRequest(req, UserDtoIn.class);
        if (!DtosInValidator.isValid(userDto).isEmpty()) {
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