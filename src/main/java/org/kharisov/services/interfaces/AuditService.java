package org.kharisov.services.interfaces;

import org.kharisov.entities.UserRecord;

import java.util.*;

/**
 * Интерфейс AuditService представляет контракт для сервиса аудита.
 */
public interface AuditService {
    /**
     * Записывает действие пользователя.
     * @param user Пользователь, выполнивший действие.
     * @param action Действие, которое нужно записать.
     */
    void addAuditRecord(UserRecord user, String action);

    /**
     * Получает журналы аудита для указанного пользователя.
     * @param user Пользователь, для которого нужно получить журналы аудита.
     * @return Список журналов аудита.
     */
    List<String> getAuditRecords(UserRecord user);

    /**
     * Получает все журналы аудита.
     * @return Map, где ключом является номер счета пользователя, а значением - список журналов аудита.
     */
    Map<String, List<String>> getAllAuditRecords();
}
