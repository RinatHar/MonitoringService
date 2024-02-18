package org.kharisov.dtos.db;

import lombok.Getter;
import lombok.Setter;
import org.kharisov.domains.ReadingType;

import java.time.LocalDate;

/**
 * Класс ReadingDto представляет собой объект передачи данных для показаний.
 */
@Getter
@Setter
public class ReadingDto {
    /**
     * Уникальный идентификатор показания.
     */
    private Long id;

    /**
     * Уникальный идентификатор пользователя, связанного с показанием.
     */
    private Long userId;

    /**
     * Номер счета, связанный с показанием.
     */
    private String accountNum;

    /**
     * Тип чтения.
     */
    private ReadingType type;

    /**
     * Уникальный идентификатор типа показания, связанного с показанием.
     */
    private Long typeId;

    /**
     * Значение показания.
     */
    private int value;

    /**
     * Дата передачи показания.
     */
    private LocalDate date;
}
