package org.kharisov.dtos;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.*;
import org.kharisov.annotations.*;
import org.kharisov.validators.groups.*;

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
    @NotNull(groups = {Default.class, AccountNumValidationGroup.class}, message = "AccountNum must not be null")
    @Size(min = 16, max = 16, message = "AccountNum must be exactly 16 characters",
            groups = {Default.class, AccountNumValidationGroup.class})
    @IsLong(message = "AccountNum is not an long")
    private String accountNum;

    /**
     * Пароль пользователя. Он должен быть от 8 до 128 символов в длину.
     */
    @NotNull(groups = {Default.class, PasswordValidationGroup.class}, message = "Password must not be null")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters",
            groups = {Default.class, PasswordValidationGroup.class})
    private String password;

    /**
     * Пароль пользователя. Он должен быть от 8 до 128 символов в длину.
     */
    @NotNull(groups = {RoleValidationGroup.class}, message = "Role must not be null")
    @RoleValue(groups = {RoleValidationGroup.class}, message = "Invalid role")
    private String role;

}
