package org.kharisov.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.kharisov.annotations.*;

/**
 * Класс ReadingDtoIn представляет собой Data Transfer Object (DTO) для передачи данных о чтении.
 * Он содержит информацию, необходимую для идентификации типа показания и его значения.
 */
@Getter
@Setter
public class ReadingDto {
    /**
     * Тип показания.
     * Он должен быть не пустым и существующим типом показания.
     */
    @NotNull
    private String type;

    /**
     * Значение показания.
     * Оно должно быть не пустым и представлять собой целое число.
     */
    @NotNull
    @IsInteger
    private String value;
}
