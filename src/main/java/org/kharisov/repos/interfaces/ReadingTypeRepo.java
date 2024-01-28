package org.kharisov.repos.interfaces;

import org.kharisov.entities.ReadingType;

import java.util.*;

/**
 * Интерфейс IndicatorTypeRepo представляет контракт для репозитория типов показаний.
 */
public interface ReadingTypeRepo {

    /**
     * Записывает тип показания в хранилище.
     * @param name Название типа показания.
     */
    void addReadingType(String name);

    /**
     * Возвращает объект типа показания из хранилища.
     * @param name Название типа показания.
     * @return Объект типа показания.
     */
    Optional<ReadingType> getReadingType(String name);

    /**
     * Возвращает названия всех типов показаний из хранилища.
     * @return Множество всех типов показаний.
     */
    Set<String> getReadingNames();
}
