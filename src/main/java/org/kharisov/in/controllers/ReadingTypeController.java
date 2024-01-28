package org.kharisov.in.controllers;

import org.kharisov.services.ReadingTypeService;
import org.kharisov.entities.ReadingType;

import java.util.*;

/**
 * Класс ReadingTypeController представляет контроллер для работы с типами показаний.
 * Этот класс предоставляет методы для добавления типов показаний, получения типа показания по имени и получения всех имен типов показаний.
 */
public class ReadingTypeController {
    /**
     * Сервис для работы с типами показаний.
     */
    private final ReadingTypeService readingTypeService;

    /**
     * Конструктор класса ReadingTypeController.
     * @param readingTypeService Сервис для работы с типами показаний.
     */
    public ReadingTypeController(ReadingTypeService readingTypeService) {
        this.readingTypeService = readingTypeService;
    }

    /**
     * Добавляет новый тип показания.
     * @param name Имя нового типа показания.
     */
    public void addReadingType(String name) {
        readingTypeService.addReadingType(name);
    }

    /**
     * Получает тип показания по имени.
     * @param name Имя типа показания.
     * @return Optional<ReadingType>, содержащий тип показания, если он существует, иначе Optional.empty().
     */
    public Optional<ReadingType> getReadingType(String name) {
        return readingTypeService.getReadingType(name);
    }

    /**
     * Получает все имена типов показаний.
     * @return Набор имен типов показаний.
     */
    public Set<String> getReadingNames() {
        return readingTypeService.getReadingNames();
    }
}

