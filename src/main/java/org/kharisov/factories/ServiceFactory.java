package org.kharisov.factories;

import org.kharisov.repos.databaseImpls.*;
import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.repos.memoryImpls.AuditMemoryRepo;
import org.kharisov.repos.memoryImpls.ReadingTypeMemoryRepo;
import org.kharisov.repos.memoryImpls.UserMemoryRepo;
import org.kharisov.services.databaseImpls.AuditDbService;
import org.kharisov.services.databaseImpls.AuthDbService;
import org.kharisov.services.databaseImpls.ReadingDbService;
import org.kharisov.services.databaseImpls.ReadingTypeDbService;
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

    /**
     * Создает сервис аутентификации для базы данных.
     * @param userRepo Репозиторий пользователей для базы данных.
     * @param roleDbRepo Репозиторий ролей для базы данных.
     * @return Объект AuthService, реализующий сервис аутентификации для базы данных.
     */
    public static AuthService createAuthService(UserDbRepo userRepo, RoleDbRepo roleDbRepo) {
        return new AuthDbService(userRepo, roleDbRepo);
    }

    /**
     * Создает сервис чтения для базы данных.
     * @param readingDbRepo Репозиторий чтения для базы данных.
     * @param readingTypeDbRepo Репозиторий типов чтения для базы данных.
     * @param userRepo Репозиторий пользователей для базы данных.
     * @return Объект ReadingService, реализующий сервис чтения для базы данных.
     */
    public static ReadingService createReadingService(ReadingDbRepo readingDbRepo,
                                                      ReadingTypeDbRepo readingTypeDbRepo,
                                                      UserDbRepo userRepo) {
        return new ReadingDbService(readingDbRepo,userRepo, readingTypeDbRepo);
    }

    /**
     * Создает сервис типов чтения для базы данных.
     * @param readingTypeDbRepo Репозиторий типов чтения для базы данных.
     * @return Объект ReadingTypeService, реализующий сервис типов чтения для базы данных.
     */
    public static ReadingTypeService createReadingTypeService(ReadingTypeDbRepo readingTypeDbRepo) {
        return new ReadingTypeDbService(readingTypeDbRepo);
    }

    /**
     * Создает сервис аудита для базы данных.
     * @param userRepo Репозиторий пользователей для базы данных.
     * @param auditDbRepo Репозиторий аудита для базы данных.
     * @return Объект AuditService, реализующий сервис аудита для базы данных.
     */
    public static AuditService createAuditService(UserDbRepo userRepo, AuditDbRepo auditDbRepo) {
        return new AuditDbService(userRepo, auditDbRepo);
    }
}