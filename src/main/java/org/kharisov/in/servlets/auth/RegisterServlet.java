package org.kharisov.in.servlets.auth;

import com.fasterxml.jackson.databind.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.dtos.in.UserDtoIn;
import org.kharisov.entities.User;
import org.kharisov.mappers.UserMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.*;
import org.kharisov.validators.DtosInValidator;

import java.io.IOException;
import java.util.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }

    public RegisterServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDtoIn userDto = DtoUtils.parseDtoFromRequest(req, UserDtoIn.class);
        if (!DtosInValidator.isValid(userDto).isEmpty()) {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }
        User user = UserMapper.INSTANCE.toEntity(userDto);
        Optional<User> optionalUser = authService.addUser(user);
        if (optionalUser.isPresent()) {
            Map<String, String> tokens = AuthUtils.createJwtForUser(user);
            ResponseUtils.sendJwtResponse(resp, tokens);
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_CONFLICT, "User already exists");
        }
    }

}