package org.kharisov.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Класс DtoUtils предоставляет утилиты для работы с DTO (Data Transfer Objects).
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>parseDtoFromRequest(HttpServletRequest req, Class<T> dtoClass): Преобразует JSON из тела запроса в DTO.</li>
 * </ul>
 */
public class DtoUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Преобразует JSON из тела запроса в DTO.
     *
     * @param req объект HttpServletRequest, который содержит запрос клиента
     * @param dtoClass класс DTO, в который должен быть преобразован JSON
     * @return DTO, преобразованный из JSON, или null, если произошла ошибка
     */
    public static <T> T parseDtoFromRequest(HttpServletRequest req, Class<T> dtoClass) {
        T dto = null;
        try {
            String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            dto = objectMapper.readValue(json, dtoClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dto;
    }
}