package org.kharisov.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.kharisov.annotations.IsLong;

/**
 * Класс ReadingDtoIn представляет собой Data Transfer Object (DTO) для передачи данных о чтении.
 * Он содержит информацию, необходимую для идентификации типа показания и его значения.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadingDto {
    /**
     * Тип показания.
     * Он должен быть не пустым и существующим типом показания.
     */
    @NotNull(message = "Type must not be null")
    private String type;

    /**
     * Значение показания.
     * Оно должно быть не пустым и представлять собой целое число.
     */
    @NotNull(message = "Value must not be null")
    @IsLong
    private String value;
}
