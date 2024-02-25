package org.kharisov.services.interfaces;

import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.dtos.ReadingDto;
import org.kharisov.entities.ReadingTypeRecord;

import java.time.LocalDate;
import java.util.*;

public interface ReadingService {
    /**
     * Добавляет показание для указанного пользователя, если оно еще не существует.
     * @param user Пользователь, для которого добавляется показание.
     * @param readingTypeRecord Тип показания.
     * @param value Значение показания.
     */
    void addReading(UserRecord user, ReadingTypeRecord readingTypeRecord, int value);

    /**
     * Проверяет, существует ли указанное показание для пользователя на текущую дату.
     * @param user Пользователь, для которого проверяется показание.
     * @param readingTypeRecord Тип показания.
     * @param now Текущая дата.
     * @return true, если показание существует, иначе false.
     */
    boolean readingExists(UserRecord user, ReadingTypeRecord readingTypeRecord, LocalDate now);

    /**
     * Получает показания указанного пользователя за указанный месяц и год.
     * @param user Пользователь, для которого получаются показания.
     * @param month Месяц, за который получаются показания.
     * @param year Год, за который получаются показания.
     * @return Список показаний чтения.
     */
    List<ReadingDto> getReadingsByMonth(UserRecord user, int month, int year);

    /**
     * Получает текущее показание чтения указанного типа для пользователя.
     * @param user Пользователь, для которого получается показание.
     * @param readingTypeRecord Тип показания.
     * @return ReadingRecord, содержащий текущее показание.
     */
    ReadingDto getCurrentReading(UserRecord user, ReadingTypeRecord readingTypeRecord);

    /**
     * Получает историю показаний для указанного пользователя.
     * @param user Пользователь, для которого получается история показаний.
     * @return Список показаний.
     */
    List<ReadingDto> getHistory(UserRecord user);

    /**
     * Получает все показания всех пользователей.
     * @return Map, где ключом является номер счета пользователя, а значением - список показаний.
     */
    Map<String, List<ReadingDto>> getAllReadings();
}
