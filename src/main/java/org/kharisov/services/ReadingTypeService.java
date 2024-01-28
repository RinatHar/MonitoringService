package org.kharisov.services;

import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.entities.ReadingType;

import java.util.*;

/**
 * Класс ReadingTypeService представляет сервис для работы с типами показаний.
 * Этот класс предоставляет методы для добавления типов показаний, получения типа показания по имени и получения всех названий типов показаний.
 */
public class ReadingTypeService {
    /**
     * Репозиторий для управления хранилищем типов показаний.
     */
    private final ReadingTypeRepo readingTypeRepo;

    /**
     * Конструктор класса ReadingTypeService.
     * @param readingTypeRepo Репозиторий для управления хранилищем типов показаний.
     */
    public ReadingTypeService(ReadingTypeRepo readingTypeRepo)  {
        this.readingTypeRepo = readingTypeRepo;
    }

    /**
     * Добавляет новый тип показания.
     * @param name Имя нового типа показания.
     */
    public void addReadingType(String name) {
        readingTypeRepo.addReadingType(name);
    }

    /**
     * Получает тип показания по имени.
     * @param name Имя типа показания.
     * @return Optional<ReadingType>, содержащий тип показания, если он существует, иначе Optional.empty().
     */
    public Optional<ReadingType> getReadingType(String name) {
        return readingTypeRepo.getReadingType(name);
    }

    /**
     * Получает все имена типов показаний.
     * @return Набор имен типов показаний.
     */
    public Set<String> getReadingNames() {
        return readingTypeRepo.getReadingNames();
    }
}

