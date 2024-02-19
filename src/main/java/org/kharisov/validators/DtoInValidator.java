package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.exceptions.InvalidDtoException;

import java.util.Set;

/**
 * Класс DtoInValidator предоставляет утилиты для валидации DTO (Data Transfer Objects).
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>{@link #validate(Object, Class[]) validate(T dto, Class<?>... groups)}: Проверяет, является ли DTO действительным.</li>
 * </ul>
 */
public class DtoInValidator {
    /**
     * Проверяет, является ли DTO действительным. Если DTO не проходит валидацию, выбрасывается исключение InvalidDtoException.
     *
     * @param dto DTO для валидации.
     * @param groups Группы валидации, которые должны быть применены.
     * @throws InvalidDtoException если DTO не проходит валидацию.
     */
    public static <T> void validate(T dto, Class<?>... groups) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(dto, groups);

        if (!violations.isEmpty()) {
            throw new InvalidDtoException("Data entered incorrectly");
        }
    }
}
