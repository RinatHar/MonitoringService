package org.kharisov.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс EntryDto представляет собой объект передачи данных для записей.
 */
@Getter
@Setter
public class EntryDto {
    /**
     * Уникальный идентификатор записи.
     */
    private Long id;

    /**
     * Действие, связанное с записью.
     */
    private String action;

    /**
     * Уникальный идентификатор пользователя, связанного с записью.
     */
    private Long userId;

    /**
     * Номер счета, связанный с записью.
     */
    private String accountNum;
}
