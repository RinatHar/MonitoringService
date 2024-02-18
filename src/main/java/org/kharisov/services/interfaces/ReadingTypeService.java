package org.kharisov.services.interfaces;

import org.kharisov.entities.ReadingTypeRecord;
import org.kharisov.exceptions.*;

import java.util.*;

public interface ReadingTypeService {

    /**
     * Добавляет новый тип показания.
     * @param readingTypeRecord Новый тип показания.
     */
    void addReadingType(ReadingTypeRecord readingTypeRecord) throws MyDatabaseException;

    /**
     * Получает тип показания по имени.
     * @param name Имя типа показания.
     * @return Optional<ReadingType>, содержащий тип показания, если он существует, иначе Optional.empty().
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     * @throws EntityNotFoundException Если тип показания с указанным именем не найден.
     */
    ReadingTypeRecord getByName(String name) throws MyDatabaseException, EntityNotFoundException;

    /**
     * Получает все имена типов показаний.
     * @return Набор имен типов показаний.
     */
    Set<String> getReadingNames() throws MyDatabaseException;
}
