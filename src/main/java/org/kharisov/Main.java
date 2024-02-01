package org.kharisov;

import org.kharisov.factories.*;
import org.kharisov.in.*;
import org.kharisov.repos.interfaces.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.storages.*;
import org.kharisov.in.controllers.*;

public class Main {
    public static void main(String[] args) {
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