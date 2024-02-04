package org.kharisov.services.interfaces;

import org.kharisov.entities.*;

import java.time.LocalDate;
import java.util.*;

public interface ReadingService {
    /**
     * Добавляет показание для указанного пользователя, если оно еще не существует.
     * @param user Пользователь, для которого добавляется показание.
     * @param reading Тип показания.
     * @param value Значение показания.
     */
    void addReading(User user, ReadingType reading, int value);

    /**
     * Проверяет, существует ли указанное показание для пользователя на текущую дату.
     * @param user Пользователь, для которого проверяется показание.
     * @param reading Тип показания.
     * @param now Текущая дата.
     * @return true, если показание существует, иначе false.
     */
    boolean readingExists(User user, ReadingType reading, LocalDate now);

    /**
     * Получает показания указанного пользователя за указанный месяц и год.
     * @param user Пользователь, для которого получаются показания.
     * @param month Месяц, за который получаются показания.
     * @param year Год, за который получаются показания.
     * @return Список показаний чтения.
     */
    List<ReadingRecord> getReadingsByMonth(User user, int month, int year);

    /**
     * Получает текущее показание чтения указанного типа для пользователя.
     * @param user Пользователь, для которого получается показание.
     * @param type Тип показания.
     * @return Optional<ReadingRecord>, содержащий текущее показание, если оно существует, иначе Optional.empty().
     */
    Optional<ReadingRecord> getCurrentReading(User user, ReadingType type);

    /**
     * Получает историю показаний для указанного пользователя.
     * @param user Пользователь, для которого получается история показаний.
     * @return Список показаний.
     */
    List<ReadingRecord> getHistory(User user);

    /**
     * Получает все показания всех пользователей.
     * @return Map, где ключом является номер счета пользователя, а значением - список показаний.
     */
    Map<String, List<ReadingRecord>> getAllReadings();
}
