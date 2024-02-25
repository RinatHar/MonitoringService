package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.annotations.RoleValue;
import org.kharisov.enums.Role;

import java.util.Arrays;

public class RoleValueValidator implements ConstraintValidator<RoleValue, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(Role.values())
                .anyMatch(role -> role.name().equals(value));
    }
}
