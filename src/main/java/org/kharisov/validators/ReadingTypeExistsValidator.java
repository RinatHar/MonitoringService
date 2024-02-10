package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.annotations.ReadingTypeExists;
import org.kharisov.entities.ReadingType;
import org.kharisov.services.interfaces.ReadingTypeService;
import org.kharisov.services.singletons.ReadingTypeServiceSingleton;

import java.util.Optional;

public class ReadingTypeExistsValidator implements ConstraintValidator<ReadingTypeExists, String> {

    private ReadingTypeService readingTypeService;

    @Override
    public void initialize(ReadingTypeExists constraint) {
        readingTypeService = ReadingTypeServiceSingleton.getInstance();
    }

    @Override
    public boolean isValid(String readingType, ConstraintValidatorContext context) {
        Optional<ReadingType> optionalReadingType = readingTypeService.getReadingType(readingType);
        return optionalReadingType.isPresent();
    }
}
