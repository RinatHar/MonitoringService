package org.kharisov.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.stream.Collectors;

public class DtoUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseDtoFromRequest(HttpServletRequest req, Class<T> dtoClass) {
        T dto = null;
        try {
            // Получаем JSON из тела запроса
            String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            // Используем Jackson для преобразования JSON в DTO
            dto = objectMapper.readValue(json, dtoClass);
        } catch (IOException e) {
            // Обработка исключений
            e.printStackTrace();
        }
        return dto;
    }
}