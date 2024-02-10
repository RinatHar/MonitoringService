package org.kharisov.annotations;

import jakarta.validation.*;
import org.kharisov.validators.IsIntegerValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsIntegerValidator.class)
public @interface IsInteger {

    String message() default "Value is not an integer";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
