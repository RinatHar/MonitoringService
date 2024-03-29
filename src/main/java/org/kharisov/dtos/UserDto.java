package org.kharisov.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import org.kharisov.validators.AccountNumValidationGroup;

/**
 * DTO User представляет собой пользователя в системе.
 * Эта DTO содержит номер счета, пароль.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    /**
     * Номер счета пользователя. Он должен быть ровно 16 символов в длину.
     */
    @NotNull(groups = AccountNumValidationGroup.class)
    @Size(min = 16, max = 16, message = "Field must be exactly 16 characters")
    private String accountNum;

    /**
     * Пароль пользователя. Он должен быть от 8 до 128 символов в длину.
     */
    @NotNull
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

}
