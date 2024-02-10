package org.kharisov.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class DtosInValidator {
    public static <T> Set<ConstraintViolation<T>> isValid(T dto) {
        // Создание валидатора
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Валидация dto
        return validator.validate(dto);
    }
}
