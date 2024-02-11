package org.kharisov.services.singletons;

import jakarta.servlet.ServletContext;
import org.kharisov.services.interfaces.AuditService;

/**
 * Класс AuditServiceSingleton представляет собой синглтон, который обеспечивает доступ к экземпляру AuditService.
 * Этот класс использует ленивую инициализацию для создания единственного экземпляра AuditService.
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>initialize(ServletContext context): Инициализирует экземпляр AuditService, используя контекст сервлета.</li>
 *   <li>getInstance(): Возвращает экземпляр AuditService.</li>
 * </ul>
 */
public class AuditServiceSingleton {
    private static AuditService instance;

    /**
     * Конструктор AuditServiceSingleton является приватным, чтобы предотвратить создание экземпляра класса.
     */
    private AuditServiceSingleton() {}

    /**
     * Инициализирует экземпляр AuditService, используя контекст сервлета.
     *
     * @param context контекст сервлета, из которого извлекается AuditService
     */
    public static void initialize(ServletContext context) {
        instance = (AuditService) context.getAttribute("auditService");
    }

    /**
     * Возвращает экземпляр AuditService.
     *
     * @return экземпляр AuditService
     */
    public static AuditService getInstance() {
        return instance;
    }
}
