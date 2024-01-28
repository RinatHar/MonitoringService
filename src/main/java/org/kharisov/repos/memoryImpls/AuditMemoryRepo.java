package org.kharisov.repos.memoryImpls;

import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.storages.AuditMemoryStorage;

import java.util.*;

/**
 * Класс AuditMemoryRepo реализует интерфейс AuditRepo.
 * Для взаимодействия с хранилищем аудита в памяти.
 */
public class AuditMemoryRepo implements AuditRepo {

    /**
     * Хранилище в памяти для данных аудита.
     */
    private final AuditMemoryStorage auditStorage;

    /**
     * Конструктор класса AuditMemoryRepo.
     * @param auditStorage Хранилище в памяти для данных аудита.
     */
    public AuditMemoryRepo(AuditMemoryStorage auditStorage) {
        this.auditStorage = auditStorage;
    }

    /**
     * Записывает действие пользователя для указанного аккаунта в хранилище в памяти.
     * @param accountNum Аккаунт для которого записывается действие аудита.
     * @param action Записываемое действие.
     */
    @Override
    public void logAction(String accountNum, String action) {
        if (!auditStorage.getStorage().containsKey(accountNum)) {
            auditStorage.getStorage().put(accountNum, new ArrayList<>());
        }
        auditStorage.getStorage().get(accountNum).add(action);
    }

    /**
     * Возвращает список действий пользователя для указанного аккаунта из хранилища в памяти.
     * @param accountNum Номер аккаунта для которого нужно получить действия пользователя.
     * @return Список действий пользователя для указанного аккаунта.
     */
    @Override
    public List<String> getLogs(String accountNum) {
        return auditStorage.getStorage().get(accountNum);
    }

    /**
     * Возвращает карту всех действий аудита из хранилища в памяти.
     * @return Карта, где ключ - это номер аккаунта, а значение - список действий пользователя для этого аккаунта.
     */
    @Override
    public Map<String, List<String>> getAllLogs() {
        return auditStorage.getStorage();
    }
}
