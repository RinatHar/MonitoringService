package org.kharisov.services.interfaces;

import org.kharisov.entities.ReadingType;

import java.util.Optional;
import java.util.Set;

public interface ReadingTypeService {

    /**
     * Добавляет новый тип показания.
     * @param name Имя нового типа показания.
     */
    void addReadingType(String name);

    /**
     * Получает тип показания по имени.
     * @param name Имя типа показания.
     * @return Optional<ReadingType>, содержащий тип показания, если он существует, иначе Optional.empty().
     */
    Optional<ReadingType> getReadingType(String name);

    /**
     * Получает все имена типов показаний.
     * @return Набор имен типов показаний.
     */
    Set<String> getReadingNames();
}
