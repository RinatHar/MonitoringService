package org.kharisov.dtos.db;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс ReadingTypeDto представляет собой объект передачи данных для типов показаний.
 */
@Getter
@Setter
public class ReadingTypeDto {
    /**
     * Уникальный идентификатор типа показания.
     */
    private Long id;

    /**
     * Название типа показания.
     */
    private String name;
}