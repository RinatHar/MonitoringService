package org.kharisov;

import org.kharisov.configs.Config;
import org.kharisov.factories.*;
import org.kharisov.in.*;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.in.controllers.*;

public class Main {

    public static void main(String[] args) {

        final String DB_URL = Config.get("database.url") + "?currentSchema=" + Config.get("database.schema");
        final String DB_USERNAME = Config.get("database.username");
        final String DB_PASSWORD = Config.get("database.password");

        ConnectionPool connectionPool = new ConnectionPool(DB_URL, DB_USERNAME, DB_PASSWORD, 5);

        AuditDbRepo auditDbRepo = RepoFactory.createAuditDbRepo(connectionPool);
        UserDbRepo userDbRepo = RepoFactory.createUserDbRepo(connectionPool);
        ReadingDbRepo readingDbRepo = RepoFactory.createReadingDbRepo(connectionPool);
        ReadingTypeDbRepo readingTypeDbRepo = RepoFactory.createReadingTypeDbRepo(connectionPool);
        RoleDbRepo roleDbRepo = RepoFactory.createRoleDbRepo(connectionPool);

        AuthService authService = ServiceFactory.createAuthService(userDbRepo, roleDbRepo);
        ReadingService readingService = ServiceFactory.createReadingService(readingDbRepo, readingTypeDbRepo, userDbRepo);
        ReadingTypeService readingTypeService = ServiceFactory.createReadingTypeService(readingTypeDbRepo);
        AuditService auditService = ServiceFactory.createAuditService(userDbRepo, auditDbRepo);

        AuthController authController = ControllerFactory.createAuthController(authService);
        ReadingController readingController = ControllerFactory.createReadingController(readingService);
        ReadingTypeController readingTypeController = ControllerFactory.createReadingTypeController(readingTypeService);
        AuditController auditController = ControllerFactory.createAuditController(auditService);

        ConsoleUtils consoleUtils = new ConsoleUtils(authController, readingController, readingTypeController, auditController);

        ConsoleInput app = new ConsoleInput(authController, readingController, readingTypeController, auditController, consoleUtils);
        app.start();
    }
}