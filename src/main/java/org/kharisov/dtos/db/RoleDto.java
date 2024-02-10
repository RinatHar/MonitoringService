package org.kharisov.dtos.db;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс RoleDto представляет собой объект передачи данных для ролей.
 */
@Getter
@Setter
public class RoleDto {
    /**
     * Уникальный идентификатор роли.
     */
    private Long id;

    /**
     * Название роли.
     */
    private String name;
}
