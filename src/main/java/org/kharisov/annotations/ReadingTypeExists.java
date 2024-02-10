package org.kharisov.annotations;

import jakarta.validation.*;
import org.kharisov.validators.ReadingTypeExistsValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReadingTypeExistsValidator.class)
public @interface ReadingTypeExists {

    String message() default "Reading type does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
