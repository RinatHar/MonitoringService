package org.kharisov.in.controllers;

import org.kharisov.entities.*;
import org.kharisov.services.ReadingService;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс ReadingController представляет контроллер для работы с показаниями.
 * Этот класс предоставляет методы для добавления показаний, получения текущего показания, получения показаний по месяцу и году, получения истории показаний, получения всех показаний и проверки существования показания.
 */
public class ReadingController {
    /**
     * Сервис для работы с показаниями.
     */
    private final ReadingService readingService;

    /**
     * Конструктор класса ReadingController.
     * @param readingService Сервис для работы с показаниями.
     */
    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    /**
     * Добавляет показание  для указанного пользователя.
     * @param user Пользователь, для которого добавляется показание.
     * @param reading Тип показания.
     * @param value Значение показания.
     */
    public void addReading(User user, ReadingType reading, int value) {
        readingService.addReading(user, reading, value);
    }

    /**
     * Получает текущее показание  указанного типа для пользователя.
     * @param user Пользователь, для которого получается показание.
     * @param type Тип показания.
     * @return Optional<ReadingRecord>, содержащий текущее показание, если оно существует, иначе Optional.empty().
     */
    public Optional<ReadingRecord> getCurrentIndicator(User user, ReadingType type) {
        return readingService.getCurrentReading(user, type);
    }

    /**
     * Получает показания указанного пользователя за указанный месяц и год.
     * @param user Пользователь, для которого получаются показания.
     * @param month Месяц, за который получаются показания.
     * @param year Год, за который получаются показания.
     * @return Список показаний.
     */
    public List<ReadingRecord> getIndicatorsByMonth(User user, int month, int year) {
        return readingService.getReadingsByMonth(user, month, year);
    }

    /**
     * Получает историю показаний для указанного пользователя.
     * @param user Пользователь, для которого получается история показаний.
     * @return Список показаний.
     */
    public List<ReadingRecord> getHistory(User user) {
        return readingService.getHistory(user);
    }

    /**
     * Получает все показания всех пользователей.
     * @return Map, где ключом является номер счета пользователя, а значением - список показаний.
     */
    public Map<String, List<ReadingRecord>> getAllReadings() {
        return readingService.getAllReadings();
    }

    /**
     * Проверяет, существует ли указанное показание для пользователя на текущую дату.
     * @param user Пользователь, для которого проверяется показание.
     * @param reading Тип показания.
     * @param now Текущая дата.
     * @return true, если показание существует, иначе false.
     */
    public boolean readingExists(User user, ReadingType reading, LocalDate now) {
        return readingService.readingExists(user, reading, now);
    }
}

