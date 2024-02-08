package org.kharisov.factories;

import org.kharisov.repos.databaseImpls.*;

/**
 * Класс RepoFactory представляет фабрику репозиториев.
 * Этот класс предоставляет статические методы для создания различных репозиториев.
 */
public class RepoFactory {
    /**
     * Создает репозиторий пользователей в базе данных.
     * @return Объект UserDbRepo, реализующий репозиторий пользователей в базе данных.
     */
    public static UserDbRepo createUserDbRepo(ConnectionPool connectionPool) {
        return new UserDbRepo(connectionPool);
    }

    /**
     * Создает репозиторий аудита в базе данных.
     * @return Объект AuditDbRepo, реализующий репозиторий аудита в базе данных.
     */
    public static AuditDbRepo createAuditDbRepo(ConnectionPool connectionPool) {
        return new AuditDbRepo(connectionPool);
    }

    /**
     * Создает репозиторий типов показаний в базе данных.
     * @return Объект ReadingTypeDbRepo, реализующий репозиторий типов показаний в базе данных.
     */
    public static ReadingTypeDbRepo createReadingTypeDbRepo(ConnectionPool connectionPool) {
        return new ReadingTypeDbRepo(connectionPool);
    }

    /**
     * Создает репозиторий показаний в базе данных.
     * @return Объект ReadingDbRepo, реализующий репозиторий показаний в базе данных.
     */
    public static ReadingDbRepo createReadingDbRepo(ConnectionPool connectionPool) {
        return new ReadingDbRepo(connectionPool);
    }

    /**
     * Создает репозиторий ролей в базе данных.
     * @return Объект RoleDbRepo, реализующий репозиторий ролей в базе данных.
     */
    public static RoleDbRepo createRoleDbRepo(ConnectionPool connectionPool) {
        return new RoleDbRepo(connectionPool);
    }
}
