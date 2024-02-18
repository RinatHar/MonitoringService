package org.kharisov.dtos.db;

import lombok.Getter;
import lombok.Setter;
import org.kharisov.enums.Role;

/**
 * Класс UserDto представляет собой объект передачи данных для пользователей.
 */
@Getter
@Setter
public class UserDto {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Номер счета пользователя.
     */
    private String accountNum;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Уникальный идентификатор роли, связанной с пользователем.
     */
    private Long roleId;

    /**
     * Роль пользователя.
     */
    private Role role;
}