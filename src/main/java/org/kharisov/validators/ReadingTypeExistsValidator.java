package org.kharisov.validators;

import jakarta.validation.*;
import org.kharisov.annotations.ReadingTypeExists;
import org.kharisov.domains.ReadingType;
import org.kharisov.services.interfaces.ReadingTypeService;
import org.kharisov.services.singletons.ReadingTypeServiceSingleton;

import java.util.Optional;

/**
 * Класс ReadingTypeExistsValidator реализует интерфейс ConstraintValidator.
 * Этот класс предоставляет функциональность для проверки существования типа показания.
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>initialize(ReadingTypeExists constraint): Инициализирует валидатор, получая экземпляр ReadingTypeService.</li>
 *   <li>isValid(String readingType, ConstraintValidatorContext context): Проверяет, существует ли указанный тип показания.</li>
 * </ul>
 *
 * @see javax.validation.ConstraintValidator
 */
public class ReadingTypeExistsValidator implements ConstraintValidator<ReadingTypeExists, String> {

    private ReadingTypeService readingTypeService;

    /**
     * Инициализирует валидатор, получая экземпляр ReadingTypeService.
     *
     * @param constraint ограничение, которое должно быть проверено
     */
    @Override
    public void initialize(ReadingTypeExists constraint) {
        readingTypeService = ReadingTypeServiceSingleton.getInstance();
    }

    /**
     * Проверяет, существует ли указанный тип показания.
     *
     * @param readingType тип показания, который должен быть проверен
     * @param context контекст валидатора ограничений
     * @return true, если указанный тип показания существует, иначе false
     */
    @Override
    public boolean isValid(String readingType, ConstraintValidatorContext context) {
        Optional<ReadingType> optionalReadingType = readingTypeService.getReadingType(readingType);
        return optionalReadingType.isPresent();
    }
}
