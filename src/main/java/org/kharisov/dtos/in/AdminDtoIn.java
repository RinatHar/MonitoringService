package org.kharisov.dtos.in;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Класс AdminDtoIn представляет собой Data Transfer Object (DTO) для передачи данных об администраторе.
 * Он содержит информацию, необходимую для идентификации администратора.
 */
@Getter
@Setter
public class AdminDtoIn {
    /**
     * Номер счета администратора.
     * Он должен быть ровно 16 символов в длину.
     */
    @NotNull
    @Size(min = 16, max = 16, message = "Field must be exactly 16 characters")
    private String accountNum;
}
