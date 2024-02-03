package org.kharisov.factories;

import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.repos.memoryImpls.AuditMemoryRepo;
import org.kharisov.repos.memoryImpls.ReadingTypeMemoryRepo;
import org.kharisov.repos.memoryImpls.UserMemoryRepo;
import org.kharisov.services.interfaces.AuditService;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.services.interfaces.ReadingService;
import org.kharisov.services.interfaces.ReadingTypeService;
import org.kharisov.services.memoryImpls.AuditMemoryService;
import org.kharisov.services.memoryImpls.AuthMemoryService;
import org.kharisov.services.memoryImpls.ReadingMemoryService;
import org.kharisov.services.memoryImpls.ReadingTypeMemoryService;

/**
 * Класс ServiceFactory представляет фабрику сервисов.
 * Этот класс предоставляет статические методы для создания различных сервисов.
 */
public class ServiceFactory {
    /**
     * Создает сервис аутентификации.
     * @param userRepo Репозиторий пользователей.
     * @return Объект AuthService, реализующий сервис аутентификации.
     */
    public static AuthService createAuthService(UserRepo userRepo) {
        if (userRepo instanceof UserMemoryRepo) {
            return new AuthMemoryService(userRepo);
        } else {
            throw new IllegalArgumentException("Unsupported repository type");
        }
    }

    /**
     * Создает сервис аудита.
     * @param auditRepo Репозиторий аудита.
     * @return Объект AuditService, реализующий сервис аудита.
     */
    public static AuditService createAuditService(AuditRepo auditRepo) {
        if (auditRepo instanceof AuditMemoryRepo) {
            return new AuditMemoryService(auditRepo);
        } else {
            throw new IllegalArgumentException("Unsupported repository type");
        }
    }

    /**
     * Создает сервис чтения.
     * @param userRepo Репозиторий пользователей.
     * @return Объект ReadingService, реализующий сервис показаний.
     */
    public static ReadingService createReadingService(UserRepo userRepo) {
        if (userRepo instanceof UserMemoryRepo) {
            return new ReadingMemoryService(userRepo);
        } else {
            throw new IllegalArgumentException("Unsupported repository type");
        }

    }

    /**
     * Создает сервис типов чтения.
     * @param readingTypeRepo Репозиторий типов показаний.
     * @return Объект ReadingTypeService, реализующий сервис типов показаний.
     */
    public static ReadingTypeService createReadingTypeService(ReadingTypeRepo readingTypeRepo) {
        if (readingTypeRepo instanceof ReadingTypeMemoryRepo) {
            return new ReadingTypeMemoryService(readingTypeRepo);
        } else {
            throw new IllegalArgumentException("Unsupported repository type");
        }
    }
}