package org.kharisov.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.dtos.db.UserDto;

import java.io.IOException;

@WebServlet("/example")
public class ExampleServlet extends HttpServlet {
    private final ObjectMapper objectMapper;

    public ExampleServlet() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserDto userDto = new UserDto();
        userDto.setAccountNum("1111");
        userDto.setPassword("2222");

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        byte[] bytes = objectMapper.writeValueAsBytes(userDto);
        resp.getOutputStream().write(bytes);

    }

}
