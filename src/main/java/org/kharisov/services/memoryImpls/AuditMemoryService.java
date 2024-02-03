package org.kharisov.services.memoryImpls;

import org.kharisov.entities.User;
import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.services.interfaces.AuditService;

import java.util.*;

/**
 * Класс AuditService представляет сервис для аудита.
 * Этот класс предоставляет методы для регистрации действий и получения журналов аудита.
 */
public class AuditMemoryService implements AuditService {
    /**
     * Репозиторий для управления хранилищем аудита.
     */
    private final AuditRepo auditMemoryRepo;

    /**
     * Конструктор класса AuditMemoryService.
     * @param auditMemoryRepo Репозиторий для управления хранилищем аудита.
     */
    public AuditMemoryService(AuditRepo auditMemoryRepo) {
        this.auditMemoryRepo = auditMemoryRepo;
    }

    /**
     * Записывает действие пользователя.
     * @param user Пользователь, выполнивший действие.
     * @param action Действие, которое нужно записать.
     */
    public void addEntry(User user, String action) {
        auditMemoryRepo.addEntry(user.getAccountNum(), action);
    }

    /**
     * Получает журналы аудита для указанного пользователя.
     * @param user Пользователь, для которого нужно получить журналы аудита.
     * @return Список журналов аудита.
     */
    public List<String> getEntries(User user) {
        return auditMemoryRepo.getEntries(user.getAccountNum());
    }

    /**
     * Получает все журналы аудита.
     * @return Map, где ключом является номер счета пользователя, а значением - список журналов аудита.
     */
    public Map<String, List<String>> getAllEntries() {
        return auditMemoryRepo.getAllEntries();
    }
}
