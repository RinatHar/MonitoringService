package org.kharisov.storages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Получить хранилище аудита.
     * @return Map, где ключ - счет пользователя, а значение - список его действий.
     */
    public Map<String, List<String>> getStorage() {
        return auditStorage;
    }
}
