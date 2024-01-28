package org.kharisov.repos.memoryImpls;

import org.kharisov.entities.ReadingType;
import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.storages.ReadingTypeMemoryStorage;

import java.util.*;

/**
 * Класс IndicatorTypeMemoryRepo реализует интерфейс IndicatorTypeRepo.
 * Для взаимодействия с хранилищем типов показаний в памяти.
 */
public class ReadingTypeMemoryRepo implements ReadingTypeRepo {

    /**
     * Хранилище для типов показаний в памяти.
     */
    private final ReadingTypeMemoryStorage storage;

    /**
     * Конструктор класса IndicatorTypeMemoryRepo.
     * @param storage Хранилище для типов показаний в памяти.
     */
    public ReadingTypeMemoryRepo(ReadingTypeMemoryStorage storage) {
        this.storage = storage;
    }

    /**
     * Записывает тип показания в хранилище в памяти.
     * @param name Название типа показания.
     */
    public void addReadingType(String name) {
        storage.getStorage().put(name, ReadingType.Create(name));
    }

    /**
     * Возвращает объект типа показания из хранилища в памяти.
     * @param name Название типа показания.
     * @return Объект типа показания.
     */
    public Optional<ReadingType> getReadingType(String name) {
        ReadingType readingType = storage.getStorage().get(name);
        if (readingType != null) {
            return Optional.of(readingType);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Возвращает названия всех типов показаний из хранилища в памяти.
     * @return Множество всех типов показаний.
     */
    public Set<String> getReadingNames() {
        return storage.getStorage().keySet();
    }
}
