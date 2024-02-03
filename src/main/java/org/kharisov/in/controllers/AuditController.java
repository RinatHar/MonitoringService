package org.kharisov.in.controllers;

import org.kharisov.entities.User;
import org.kharisov.services.interfaces.AuditService;

import java.util.*;

/**
 * Класс AuditController представляет контроллер для аудита.
 * Этот класс предоставляет методы для регистрации действий, получения журналов аудита для конкретного пользователя и получения всех журналов аудита.
 */
public class AuditController {
    /**
     * Сервис для работы с аудитом.
     */
    private final AuditService auditService;

    /**
     * Конструктор класса AuditController.
     * @param auditService Сервис для работы с аудитом.
     */
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Регистрирует действие пользователя.
     * @param user Пользователь, выполнивший действие.
     * @param action Действие, которое нужно зарегистрировать.
     */
    public void addEntry(User user, String action) {
        auditService.addEntry(user, action);
    }

    /**
     * Получает журналы аудита для указанного пользователя.
     * @param user Пользователь, для которого нужно получить журналы аудита.
     * @return Список журналов аудита.
     */
    public List<String> getEntries(User user) {
        return auditService.getEntries(user);
    }

    /**
     * Получает все журналы аудита.
     * @return Map, где ключом является номер счета пользователя, а значением - список журналов аудита.
     */
    public Map<String, List<String>> getAllEntries() {
        return auditService.getAllEntries();
    }
}

