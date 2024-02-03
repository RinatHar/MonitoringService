package org.kharisov.factories;

import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.repos.memoryImpls.AuditMemoryRepo;
import org.kharisov.repos.memoryImpls.ReadingTypeMemoryRepo;
import org.kharisov.repos.memoryImpls.UserMemoryRepo;
import org.kharisov.storages.AuditMemoryStorage;
import org.kharisov.storages.ReadingTypeMemoryStorage;
import org.kharisov.storages.UserMemoryStorage;

/**
 * Класс RepoFactory представляет фабрику репозиториев.
 * Этот класс предоставляет статические методы для создания различных репозиториев.
 */
public class RepoFactory {
    /**
     * Создает репозиторий пользователей.
     * @param userMemoryStorage Хранилище пользователей в памяти.
     * @return Объект UserRepo, реализующий репозиторий пользователей.
     */
    public static UserRepo createUserMemoryRepo(UserMemoryStorage userMemoryStorage) {
        return new UserMemoryRepo(userMemoryStorage);
    }

    /**
     * Создает репозиторий аудита.
     * @param auditMemoryStorage Хранилище аудита в памяти.
     * @return Объект AuditRepo, реализующий репозиторий аудита.
     */
    public static AuditRepo createAuditMemoryRepo(AuditMemoryStorage auditMemoryStorage) {
        return new AuditMemoryRepo(auditMemoryStorage);
    }

    /**
     * Создает репозиторий типов показаний.
     * @param readingTypeMemoryStorage Хранилище типов показаний в памяти.
     * @return Объект ReadingTypeRepo, реализующий репозиторий типов показаний.
     */
    public static ReadingTypeRepo createReadingTypeMemoryRepo(ReadingTypeMemoryStorage readingTypeMemoryStorage) {
        return new ReadingTypeMemoryRepo(readingTypeMemoryStorage);
    }
}
