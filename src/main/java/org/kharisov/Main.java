package org.kharisov;

import org.kharisov.in.ConsoleInput;
import org.kharisov.in.ConsoleUtils;
import org.kharisov.repos.interfaces.*;
import org.kharisov.repos.memoryImpls.*;
import org.kharisov.storages.*;
import org.kharisov.services.*;
import org.kharisov.in.controllers.*;

public class Main {
    public static void main(String[] args) {
        UserMemoryStorage userStorage = new UserMemoryStorage();
        ReadingTypeMemoryStorage indicatorTypeStorage = new ReadingTypeMemoryStorage();
        AuditMemoryStorage auditStorage = new AuditMemoryStorage();

        UserRepo userMemoryRepo = new UserMemoryRepo(userStorage);
        ReadingTypeRepo readingTypeRepo = new ReadingTypeMemoryRepo(indicatorTypeStorage);
        AuditRepo auditRepo = new AuditMemoryRepo(auditStorage);

        AuthService authService = new AuthService(userMemoryRepo);
        ReadingService readingService = new ReadingService(userMemoryRepo);
        ReadingTypeService readingTypeService = new ReadingTypeService(readingTypeRepo);
        AuditService auditService = new AuditService(auditRepo);

        AuthController authController = new AuthController(authService);
        ReadingController readingController = new ReadingController(readingService);
        ReadingTypeController readingTypeController = new ReadingTypeController(readingTypeService);
        AuditController auditController = new AuditController(auditService);

        ConsoleUtils consoleUtils = new ConsoleUtils(authController, readingController, readingTypeController, auditController);

        ConsoleInput app = new ConsoleInput(authController, readingController, readingTypeController, auditController, consoleUtils);
        app.start();
    }
}