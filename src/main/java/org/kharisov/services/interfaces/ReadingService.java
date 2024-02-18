package org.kharisov.services.interfaces;

import org.kharisov.entities.*;
import org.kharisov.exceptions.*;

import java.time.LocalDate;
import java.util.*;

public interface ReadingService {
    /**
     * Добавляет показание для указанного пользователя, если оно еще не существует.
     * @param user Пользователь, для которого добавляется показание.
     * @param readingTypeRecord Тип показания.
     * @param value Значение показания.
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     * @throws ConflictException Если показание за текущий месяц уже существует.
     * @throws EntityNotFoundException Если текущий пользователь или тип показания не найден.
     */
    void addReading(UserRecord user, ReadingTypeRecord readingTypeRecord, int value)
            throws MyDatabaseException, EntityNotFoundException, ConflictException;

    /**
     * Проверяет, существует ли указанное показание для пользователя на текущую дату.
     * @param user Пользователь, для которого проверяется показание.
     * @param readingTypeRecord Тип показания.
     * @param now Текущая дата.
     * @return true, если показание существует, иначе false.
     */
    boolean readingExists(UserRecord user, ReadingTypeRecord readingTypeRecord, LocalDate now) throws MyDatabaseException;

    /**
     * Получает показания указанного пользователя за указанный месяц и год.
     * @param user Пользователь, для которого получаются показания.
     * @param month Месяц, за который получаются показания.
     * @param year Год, за который получаются показания.
     * @return Список показаний чтения.
     */
    List<UserReadingRecord> getReadingsByMonth(UserRecord user, int month, int year) throws MyDatabaseException;

    /**
     * Получает текущее показание чтения указанного типа для пользователя.
     * @param user Пользователь, для которого получается показание.
     * @param readingTypeRecord Тип показания.
     * @return ReadingRecord, содержащий текущее показание.
     */
    UserReadingRecord getCurrentReading(UserRecord user, ReadingTypeRecord readingTypeRecord) throws MyDatabaseException;

    /**
     * Получает историю показаний для указанного пользователя.
     * @param user Пользователь, для которого получается история показаний.
     * @return Список показаний.
     */
    List<UserReadingRecord> getHistory(UserRecord user) throws MyDatabaseException;

    /**
     * Получает все показания всех пользователей.
     * @return Map, где ключом является номер счета пользователя, а значением - список показаний.
     */
    Map<String, List<UserReadingRecord>> getAllReadings() throws MyDatabaseException;
}
