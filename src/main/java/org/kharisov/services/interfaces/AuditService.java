package org.kharisov.services.interfaces;

import org.kharisov.entities.User;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс AuditService представляет контракт для сервиса аудита.
 */
public interface AuditService {
    /**
     * Записывает действие пользователя.
     * @param user Пользователь, выполнивший действие.
     * @param action Действие, которое нужно записать.
     */
    public void addEntry(User user, String action);

    /**
     * Получает журналы аудита для указанного пользователя.
     * @param user Пользователь, для которого нужно получить журналы аудита.
     * @return Список журналов аудита.
     */
    public List<String> getEntries(User user);

    /**
     * Получает все журналы аудита.
     * @return Map, где ключом является номер счета пользователя, а значением - список журналов аудита.
     */
    public Map<String, List<String>> getAllEntries();
}
