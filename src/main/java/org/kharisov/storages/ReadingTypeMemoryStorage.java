package org.kharisov.storages;

import org.kharisov.entities.ReadingType;

import java.util.*;

/**
 * Класс ReadingTypeMemoryStorage представляет реализацию хранилища типов показаний (например: Холодная вода).
 * Этот класс использует Map для хранения объектов ReadingType.
 */
public class ReadingTypeMemoryStorage {

    /**
     * Хранилище типов показаний.
     * Ключ - название показания, значение - объект ReadingType.
     */
    private final Map<String, ReadingType> indicatorTypes = new HashMap<>();

    private static ReadingTypeMemoryStorage instance;

    private ReadingTypeMemoryStorage() {}

    public static ReadingTypeMemoryStorage getInstance() {
        if (instance == null) {
            instance = new ReadingTypeMemoryStorage();
        }
        return instance;
    }

    /**
     * Метод для получения хранилища типов показаний.
     * @return Map, где ключ - название показания в виде строки, а значение - объект ReadingType.
     */
    public Map<String, ReadingType> getStorage() {
        return indicatorTypes;
    }
}
