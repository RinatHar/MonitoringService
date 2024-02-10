package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.annotations.IsInteger;

public class IsIntegerValidator implements ConstraintValidator<IsInteger, String> {

    @Override
    public void initialize(IsInteger constraint) {
    }

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