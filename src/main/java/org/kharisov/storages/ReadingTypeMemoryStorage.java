package org.kharisov.storages;

import org.kharisov.entities.ReadingType;

import java.util.*;

/**
 * Класс представляет реализацию хранилища типов показаний (например: Холодная вода).
 * Этот класс использует Map для хранения объектов IndicatorType.
 */
public class ReadingTypeMemoryStorage {

    /**
     * Хранилище типов показаний.
     * Ключ - название показания, значение - объект IndicatorType.
     */
    private final Map<String, ReadingType> indicatorTypes = new HashMap<>();

    /**
     * Получить хранилище типов показаний.
     * @return Map, где ключ - номер счета в виде строки, а значение - объект IndicatorType.
     */
    public Map<String, ReadingType> getStorage() {
        return indicatorTypes;
    }
}
