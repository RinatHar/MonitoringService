package org.kharisov.repos.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс AuditRepo представляет контракт для репозитория аудита.
 */
public interface AuditRepo {

    /**
     * Записывает действие пользователя для указанного аккаунта из хранилища.
     * @param accountNum Аккаунт для которого записывается действие аудита.
     * @param action Записываемое действие.
     */
    void logAction(String accountNum, String action);

    /**
     * Возвращает список действий пользователя для указанного ключа из хранилища.
     * @param accountNum Номер аккаунта для которого нужно получить действия пользователя.
     * @return Список действий пользователя для указанного аккаунта.
     */
    List<String> getLogs(String accountNum);

    /**
     * Возвращает карту всех действий аудита из хранилища.
     * @return Карта, где ключ - это номер аккаунта, а значение - список действий пользователя для этого аккаунта.
     */
    Map<String, List<String>> getAllLogs();
}