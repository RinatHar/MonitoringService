package org.kharisov.repos.interfaces;

import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;

import java.util.*;

/**
 * Класс ReadingRepo представляет собой контракт для работы с показаниями.
 */
public interface ReadingRepo {

    /**
     * Добавляет показание и возвращает его.
     *
     * @param record Объект сущности показания для добавления.
     * @return Объект сущности добавленного показания или пустой Optional, если добавление не удалось.
     */
    Optional<ReadingRecord> add(ReadingRecord record) throws MyDatabaseException;

    /**
     * Возвращает все показания для указанного номера счета.
     *
     * @param accountNum Номер счета, для которого требуется получить показания.
     * @return Список объектов сущности показаний для указанного номера счета.
     */
    List<UserReadingRecord> getAllByAccountNum(String accountNum) throws MyDatabaseException;

    /**
     * Возвращает все показания.
     *
     * @return Список всех объектов сущности показаний.
     */
    List<UserReadingRecord> getAll() throws MyDatabaseException;
}
