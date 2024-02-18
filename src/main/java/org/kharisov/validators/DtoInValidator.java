package org.kharisov.validators;

import jakarta.validation.*;

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
    /**
     * Проверяет, является ли DTO действительным.
     *
     * @param dto DTO для проверки
     * @return Набор нарушений ограничений. Если DTO действителен, набор будет пустым.
     */
    public static <T> Set<ConstraintViolation<T>> isValid(T dto) {
        // Создание валидатора
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Валидация dto
        return validator.validate(dto);
    }
}
