package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.exceptions.InvalidDtoException;

import java.util.Set;

/**
 * Класс DtosInValidator предоставляет утилиты для валидации DTO (Data Transfer Objects).
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>isValid(T dto): Проверяет, является ли DTO действительным.</li>
 * </ul>
 */
public class DtoInValidator {
    public static <T> void validate(T dto, Class<?>... groups) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(dto, groups);

        if (!violations.isEmpty()) {
            throw new InvalidDtoException("Некорректно введены данные");
        }
    }
}
