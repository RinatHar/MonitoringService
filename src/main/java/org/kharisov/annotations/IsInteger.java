package org.kharisov.annotations;

import jakarta.validation.*;
import org.kharisov.validators.IsIntegerValidator;

import java.lang.annotation.*;

/**
 * Аннотация IsInteger используется для проверки, является ли значение поля целым числом.
 * Она использует класс IsIntegerValidator для выполнения валидации.
 *
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 * @see jakarta.validation.Constraint
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsIntegerValidator.class)
public @interface IsInteger {

    /**
     * Сообщение, которое будет отображено, если значение поля не является целым числом.
     *
     * @return строка с сообщением об ошибке.
     */
    String message() default "Value is not an integer";

    /**
     * Группы валидации, к которым относится данная аннотация.
     *
     * @return массив классов групп валидации.
     */
    Class<?>[] groups() default {};

    /**
     * Позволяет указать дополнительную информацию о нарушении ограничений.
     *
     * @return массив классов, расширяющих Payload.
     */
    Class<? extends Payload>[] payload() default {};
}
