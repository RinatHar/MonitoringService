package org.kharisov;

import liquibase.exception.CommandExecutionException;
import liquibase.exception.DatabaseException;
import org.kharisov.dtos.*;
import org.kharisov.factories.*;
import org.kharisov.in.*;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.repos.interfaces.*;
import org.kharisov.services.databaseImpls.AuditDbService;
import org.kharisov.services.databaseImpls.AuthDbService;
import org.kharisov.services.databaseImpls.ReadingDbService;
import org.kharisov.services.databaseImpls.ReadingTypeDbService;
import org.kharisov.services.interfaces.*;
import org.kharisov.storages.*;
import org.kharisov.in.controllers.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        AuditDbRepo auditDbRepo = RepoFactory.createAuditDbRepo();
        UserDbRepo userDbRepo = RepoFactory.createUserDbRepo();
        ReadingDbRepo readingDbRepo = RepoFactory.createReadingDbRepo();
        ReadingTypeDbRepo readingTypeDbRepo = RepoFactory.createReadingTypeDbRepo();
        RoleDbRepo roleDbRepo = RepoFactory.createRoleDbRepo();

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