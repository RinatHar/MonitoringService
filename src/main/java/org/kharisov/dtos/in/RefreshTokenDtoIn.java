package org.kharisov.dtos.in;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Класс RefreshTokenDtoIn представляет собой объект передачи данных (DTO),
 * который используется для передачи обновленного токена.
 */
@Getter
@Setter
public class RefreshTokenDtoIn {
    /**
     * Обновленный токен. Это поле не может быть null и должно содержать от 100 до 200 символов.
     */
    @NotNull(message = "Refresh token cannot be null")
    @Size(min = 100, max = 200, message = "Refresh token must be between 100 and 200 characters")
    private String refreshToken;
}
