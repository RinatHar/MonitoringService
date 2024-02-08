package org.kharisov.factories;

import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.databaseImpls.*;
import org.kharisov.services.interfaces.*;

/**
 * Класс ServiceFactory представляет фабрику сервисов.
 * Этот класс предоставляет статические методы для создания различных сервисов.
 */
public class ServiceFactory {
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