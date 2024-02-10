package org.kharisov.dtos.in;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO User представляет собой пользователя в системе.
 * Эта DTO содержит номер счета, пароль.
 */
@Getter
@Setter
public class UserDtoIn {
    /**
     * Номер счета пользователя.
     */
    @NotNull
    @Size(min = 16, max = 16, message = "Field must be exactly 16 characters")
    private String accountNum;

    /**
     * Пароль пользователя.
     */
    @NotNull
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

}
