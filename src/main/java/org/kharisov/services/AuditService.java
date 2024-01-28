package org.kharisov.services;

import org.kharisov.entities.User;
import org.kharisov.repos.interfaces.AuditRepo;

import java.util.*;

/**
 * Класс AuditService представляет сервис для аудита.
 * Этот класс предоставляет методы для регистрации действий и получения журналов аудита.
 */
public class AuditService {
    /**
     * Репозиторий для управления хранилищем аудита.
     */
    private final AuditRepo auditMemoryRepo;

    /**
     * Конструктор класса AuditService.
     * @param auditMemoryRepo Репозиторий для управления хранилищем аудита.
     */
    public AuditService(AuditRepo auditMemoryRepo) {
        this.auditMemoryRepo = auditMemoryRepo;
    }

    /**
     * Записывает действие пользователя.
     * @param user Пользователь, выполнивший действие.
     * @param action Действие, которое нужно записать.
     */
    public void logAction(User user, String action) {
        auditMemoryRepo.logAction(user.getAccountNum(), action);
    }

    /**
     * Получает журналы аудита для указанного пользователя.
     * @param user Пользователь, для которого нужно получить журналы аудита.
     * @return Список журналов аудита.
     */
    public List<String> getLogs(User user) {
        return auditMemoryRepo.getLogs(user.getAccountNum());
    }

    /**
     * Получает все журналы аудита.
     * @return Map, где ключом является номер счета пользователя, а значением - список журналов аудита.
     */
    public Map<String, List<String>> getAllLogs() {
        return auditMemoryRepo.getAllLogs();
    }
}
