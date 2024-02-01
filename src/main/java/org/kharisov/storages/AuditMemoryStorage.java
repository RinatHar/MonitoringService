package org.kharisov.storages;

import java.util.*;

/**
 * Класс представляет реализацию хранилища аудита.
 * Этот класс использует Map для хранения списка действий пользователя.
 */
public class AuditMemoryStorage {

    /**
     * Хранилище аудита.
     * Ключ - счет пользователя, значение - список действий.
     */
    private final Map<String, List<String>> auditStorage = new HashMap<>();

    private static AuditMemoryStorage instance;

    private AuditMemoryStorage() {}

    public static AuditMemoryStorage getInstance() {
        if (instance == null) {
            instance = new AuditMemoryStorage();
        }
        return instance;
    }
    /**
     * Получить хранилище аудита.
     * @return Map, где ключ - счет пользователя, а значение - список его действий.
     */
    public Map<String, List<String>> getStorage() {
        return auditStorage;
    }
}
