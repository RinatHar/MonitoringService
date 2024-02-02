package org.kharisov.factories;

import org.kharisov.in.controllers.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.services.memoryImpls.AuditMemoryService;
import org.kharisov.services.memoryImpls.AuthMemoryService;
import org.kharisov.services.memoryImpls.ReadingMemoryService;
import org.kharisov.services.memoryImpls.ReadingTypeMemoryService;

/**
 * Класс ControllerFactory представляет фабрику контроллеров.
 * Этот класс предоставляет статические методы для создания различных контроллеров.
 */
public class ControllerFactory {
    /**
     * Создает контроллер аутентификации.
     * @param authService Сервис аутентификации.
     * @return Объект AuthController, реализующий контроллер аутентификации.
     */
    public static AuthController createAuthController(AuthService authService) {
        if (authService instanceof AuthMemoryService) {
            return new AuthController(authService);
        } else {
            throw new IllegalArgumentException("Unsupported service type");
        }
    }

    /**
     * Создает контроллер аудита.
     * @param auditService Сервис аудита.
     * @return Объект AuditController, реализующий контроллер аудита.
     */
    public static AuditController createAuditController(AuditService auditService) {
        if (auditService instanceof AuditMemoryService) {
            return new AuditController(auditService);
        } else {
            throw new IllegalArgumentException("Unsupported service type");
        }
    }

    /**
     * Создает контроллер показаний.
     * @param readingService Сервис показаний.
     * @return Объект ReadingController, реализующий контроллер показаний.
     */
    public static ReadingController createReadingController(ReadingService readingService) {
        if (readingService instanceof ReadingMemoryService) {
            return new ReadingController(readingService);
        } else {
            throw new IllegalArgumentException("Unsupported service type");
        }
    }

    /**
     * Создает контроллер типов чтения.
     * @param readingTypeService Сервис типов показаний.
     * @return Объект ReadingTypeController, реализующий контроллер типов показаний.
     */
    public static ReadingTypeController createReadingTypeController(ReadingTypeService readingTypeService) {
        if (readingTypeService instanceof ReadingTypeMemoryService) {
            return new ReadingTypeController(readingTypeService);
        } else {
            throw new IllegalArgumentException("Unsupported service type");
        }
    }
}
