package org.kharisov.repos.interfaces;

import org.kharisov.entities.ReadingTypeRecord;
import org.kharisov.exceptions.MyDatabaseException;

import java.util.*;

/**
 * Класс ReadingTypeRepo представляет собой контракт для работы с типами показаний.
 */
public interface ReadingTypeRepo {

    /**
     * Добавляет тип чтения и возвращает его.
     *
     * @param record Объект сущности типа показания для добавления.
     * @return Объект сущности добавленного типа показания или пустой Optional, если добавление не удалось.
     */
    Optional<ReadingTypeRecord> add(ReadingTypeRecord record) throws MyDatabaseException;

    /**
     * Возвращает тип показания по его имени.
     *
     * @param name Имя типа показания, который требуется получить.
     * @return Объект сущности типа показания с указанным именем или пустой Optional, если такого типа показания не существует.
     */
    Optional<ReadingTypeRecord> getByName(String name) throws MyDatabaseException;

    /**
     * Возвращает все типы показаний.
     *
     * @return Список всех объектов сущности типов показаний.
     */
    List<ReadingTypeRecord> getAll() throws MyDatabaseException;
}
