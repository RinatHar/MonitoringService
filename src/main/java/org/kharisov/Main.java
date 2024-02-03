package org.kharisov;

import liquibase.exception.CommandExecutionException;
import liquibase.exception.DatabaseException;
import org.kharisov.factories.*;
import org.kharisov.in.*;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.interfaces.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.storages.*;
import org.kharisov.in.controllers.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            LiquibaseExample.runMigration();
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }

        UserMemoryStorage userMemoryStorage = UserMemoryStorage.getInstance();
        ReadingTypeMemoryStorage readingTypeMemoryStorage = ReadingTypeMemoryStorage.getInstance();
        AuditMemoryStorage auditMemoryStorage = AuditMemoryStorage.getInstance();

        UserRepo userMemoryRepo = RepoFactory.createUserMemoryRepo(userMemoryStorage);
        ReadingTypeRepo readingTypeMemoryRepo = RepoFactory.createReadingTypeMemoryRepo(readingTypeMemoryStorage);
        AuditRepo auditMemoryRepo = RepoFactory.createAuditMemoryRepo(auditMemoryStorage);


        AuthService authMemoryService = ServiceFactory.createAuthService(userMemoryRepo);
        ReadingService readingMemoryService = ServiceFactory.createReadingService(userMemoryRepo);
        ReadingTypeService readingTypeMemoryService = ServiceFactory.createReadingTypeService(readingTypeMemoryRepo);
        AuditService auditMemoryService = ServiceFactory.createAuditService(auditMemoryRepo);

        AuthController authController = ControllerFactory.createAuthController(authMemoryService);
        ReadingController readingController = ControllerFactory.createReadingController(readingMemoryService);
        ReadingTypeController readingTypeController = ControllerFactory.createReadingTypeController(readingTypeMemoryService);
        AuditController auditController = ControllerFactory.createAuditController(auditMemoryService);

        ConsoleUtils consoleUtils = new ConsoleUtils(authController, readingController, readingTypeController, auditController);

        ConsoleInput app = new ConsoleInput(authController, readingController, readingTypeController, auditController, consoleUtils);
        app.start();
    }
}