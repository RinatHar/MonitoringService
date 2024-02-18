package org.kharisov.utils;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Класс ResponseUtils предоставляет утилиты для отправки ответов клиенту.
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>sendSuccessResponse(HttpServletResponse resp, Object data): Отправляет успешный ответ с данными в формате JSON.</li>
 *   <li>sendErrorResponse(HttpServletResponse resp, int statusCode, String errorMessage): Отправляет ответ об ошибке с кодом состояния и сообщением об ошибке.</li>
 *   <li>sendJwtResponse(HttpServletResponse resp, Map<String, String> tokens): Отправляет ответ с JWT в формате JSON.</li>
 * </ul>
 */
public class ResponseUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    /**
     * Отправляет успешный ответ с данными в формате JSON.
     *
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @param data данные для отправки в ответе
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static void sendSuccessResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        byte[] bytes = objectMapper.writeValueAsBytes(data);
        resp.getOutputStream().write(bytes);
    }

    /**
     * Отправляет ответ об ошибке с кодом состояния и сообщением об ошибке.
     *
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @param statusCode код состояния для отправки в ответе
     * @param errorMessage сообщение об ошибке для отправки в ответе
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static void sendErrorResponse(HttpServletResponse resp, int statusCode, String errorMessage) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        String errorJson = objectMapper.writeValueAsString(errorMessage);
        resp.getOutputStream().write(errorJson.getBytes());
    }

    /**
     * Отправляет ответ с JWT в формате JSON.
     *
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @param tokens JWT для отправки в ответе
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public static void sendJwtResponse(HttpServletResponse resp, Map<String, String> tokens) throws IOException {
        ObjectNode jwtJson = objectMapper.createObjectNode();
        jwtJson.put("accessToken", tokens.get("accessToken"));
        jwtJson.put("refreshToken", tokens.get("refreshToken"));

        String jwtJsonString = objectMapper.writeValueAsString(jwtJson);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getOutputStream().write(jwtJsonString.getBytes());
    }
}
