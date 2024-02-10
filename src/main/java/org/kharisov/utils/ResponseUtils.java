package org.kharisov.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class ResponseUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendSuccessResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        byte[] bytes = objectMapper.writeValueAsBytes(data);
        resp.getOutputStream().write(bytes);
    }

    public static void sendErrorResponse(HttpServletResponse resp, int statusCode, String errorMessage) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        String errorJson = objectMapper.writeValueAsString(errorMessage);
        resp.getOutputStream().write(errorJson.getBytes());
    }

    public static void sendJwtResponse(HttpServletResponse resp, Map<String, String> tokens) throws IOException {
        // Создание объекта JSON, который содержит JWT
        ObjectNode jwtJson = objectMapper.createObjectNode();
        jwtJson.put("accessToken", tokens.get("accessToken"));
        jwtJson.put("refreshToken", tokens.get("refreshToken"));

        // Преобразование объекта JSON в строку
        String jwtJsonString = objectMapper.writeValueAsString(jwtJson);

        // Отправка JSON с JWT в теле ответа
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getOutputStream().write(jwtJsonString.getBytes());
    }
}
