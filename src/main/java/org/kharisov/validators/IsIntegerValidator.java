package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.annotations.IsInteger;

/**
 * Класс IsIntegerValidator реализует интерфейс ConstraintValidator.
 * Этот класс предоставляет функциональность для проверки, является ли строка целым числом.
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>initialize(IsInteger constraint): Инициализирует валидатор. В этом случае метод пуст, так как нет необходимости в инициализации.</li>
 *   <li>isValid(String value, ConstraintValidatorContext context): Проверяет, является ли значение действительным целым числом.</li>
 * </ul>
 *
 * @see javax.validation.ConstraintValidator
 */
public class IsIntegerValidator implements ConstraintValidator<IsInteger, String> {

    /**
     * Инициализирует валидатор. В этом случае метод пуст, так как нет необходимости в инициализации.
     *
     * @param constraint ограничение, которое должно быть проверено
     */
    @Override
    public void initialize(IsInteger constraint) {
    }

    /**
     * Проверяет, является ли значение действительным целым числом.
     *
     * @param value значение, которое должно быть проверено
     * @param context контекст валидатора ограничений
     * @return true, если значение является действительным целым числом, иначе false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}