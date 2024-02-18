package org.kharisov.repos.interfaces;

import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;

import java.util.*;

/**
 * Класс AuditRepo представляет собой контракт для работы с записями аудита.
 */
public interface AuditRepo {

    /**
     * Добавляет запись аудита и возвращает ее.
     *
     * @param record Объект сущности записи аудита для добавления.
     * @return Объект сущности добавленной записи аудита или пустой Optional, если добавление не удалось.
     */
    Optional<AuditRecord> add(AuditRecord record);

    /**
     * Возвращает все записи аудита для указанного номера счета.
     *
     * @param accountNum Номер счета, для которого требуется получить записи аудита.
     * @return Список объектов сущностей записей аудита с указанным номером счета.
     */
    List<UserAuditRecord> getAuditRecordsByAccountNum(String accountNum);

    /**
     * Возвращает все записи аудита.
     *
     * @return Список всех объектов сущностей записей аудита.
     */
    List<UserAuditRecord> getAll();
}