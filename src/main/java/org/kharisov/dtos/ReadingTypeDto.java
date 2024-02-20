package org.kharisov.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Класс ReadingTypeDtoIn представляет собой Data Transfer Object (DTO) для передачи данных о типе показания.
 * Он содержит информацию, необходимую для идентификации имени типа показания.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadingTypeDto {
    /**
     * Имя типа показания.
     * Оно должно быть не пустым.
     */
    @NotNull
    private String name;
}
