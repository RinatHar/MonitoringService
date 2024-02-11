package org.kharisov.dtos.in;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Класс ReadingTypeDtoIn представляет собой Data Transfer Object (DTO) для передачи данных о типе чтения.
 * Он содержит информацию, необходимую для идентификации имени типа чтения.
 */
@Getter
@Setter
public class ReadingTypeDtoIn {
    /**
     * Имя типа чтения.
     * Оно должно быть не пустым.
     */
    @NotNull
    private String name;
}
