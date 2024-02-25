package org.kharisov.annotations;

import jakarta.validation.*;
import org.kharisov.validators.RoleValueValidator;

import java.lang.annotation.*;

/**
 * Аннотация RoleValue используется для проверки, является ли значение поля допустимой ролью.
 * Она использует класс RoleValueValidator для выполнения валидации.
 *
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 * @see jakarta.validation.Constraint
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValueValidator.class)
public @interface RoleValue {

    /**
     * Сообщение, которое будет отображено, если значение поля не является допустимой ролью.
     *
     * @return строка с сообщением об ошибке.
     */
    String message() default "Value is not a valid role";

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
