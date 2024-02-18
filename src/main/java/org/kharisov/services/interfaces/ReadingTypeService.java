package org.kharisov.services.interfaces;

import org.kharisov.entities.ReadingTypeRecord;

import java.util.Set;

public interface ReadingTypeService {

    /**
     * Добавляет новый тип показания.
     *
     * @param readingTypeRecord Новый тип показания.
     * @return ReadingTypeRecord Добавленный тип показания
     */
    ReadingTypeRecord addReadingType(ReadingTypeRecord readingTypeRecord);

    /**
     * Получает тип показания по имени.
     * @param name Имя типа показания.
     * @return Optional<ReadingType>, содержащий тип показания, если он существует, иначе Optional.empty().
     */
    ReadingTypeRecord getByName(String name);

    /**
     * Получает все имена типов показаний.
     * @return Набор имен типов показаний.
     */
    Set<String> getReadingNames();
}
