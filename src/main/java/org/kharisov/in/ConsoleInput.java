package org.kharisov.in;

import org.kharisov.entities.*;
import org.kharisov.in.controllers.*;
import org.kharisov.repositories.*;
import org.kharisov.services.*;
import org.kharisov.storages.*;

import java.util.*;

public class ConsoleInput {
    private final UserStorageMemory userStorage = new UserStorageMemory();
    private final IndicatorTypeStorageMemory indicatorTypeStorage = new IndicatorTypeStorageMemory();

    private final UserRepo userMemoryRepo = new UserMemoryRepo(userStorage);
    private final IndicatorTypeRepo indicatorTypeRepo = new IndicatorTypeMemoryRepo(indicatorTypeStorage);

    private final AuthService authService = new AuthService(userMemoryRepo);
    private final IndicatorService indicatorService = new IndicatorService(userMemoryRepo);
    private final IndicatorTypeService indicatorTypeService = new IndicatorTypeService(indicatorTypeRepo);

    private final AuthController authController = new AuthController(authService);
    private final IndicatorController indicatorController = new IndicatorController(indicatorService);
    private final IndicatorTypeController indicatorTypeController = new IndicatorTypeController(indicatorTypeService);

    private final ConsoleUtils consoleUtils = new ConsoleUtils(
            authController,
            indicatorController,
            indicatorTypeController);

    private User currentUser;

    public void start() {
        initAdminAndIndicatorTypes();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showMenuForUnauthenticatedUser(scanner);
            } else if (currentUser.isAdmin()) {
                running = showMenuForAdmin(scanner);
            } else {
                running = showMenuForAuthenticatedUser(scanner);
            }
        }
    }

    private void initAdminAndIndicatorTypes() {
        authController.addAdmin("admin", "admin");
        indicatorTypeController.addIndicatorType("Горячая вода");
        indicatorTypeController.addIndicatorType("Холодная вода");
        indicatorTypeController.addIndicatorType("Отопление");
    }

    private boolean showMenuForUnauthenticatedUser(Scanner scanner) {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("3. Выход");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                currentUser = consoleUtils.register(scanner, currentUser);
                return true;
            }
            case "2" -> {
                currentUser = consoleUtils.login(scanner, currentUser);
                return true;
            }
            case "3" -> {
                {
                    return false;
                }
            }
            default -> {
                System.out.println("Неизвестная команда");
                return true;
            }
        }
    }

    private boolean showMenuForAuthenticatedUser(Scanner scanner) {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Сменить пользователя");
        System.out.println("3. Отправить данные");
        System.out.println("4. Просмотреть текущие данные");
        System.out.println("5. Просмотреть данные за месяц");
        System.out.println("6. Просмотреть историю");
        System.out.println("7. Выход");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                currentUser = consoleUtils.register(scanner, currentUser);
                return true;
            }
            case "2" -> {
                currentUser = consoleUtils.login(scanner, currentUser);
                return true;
            }
            case "3" -> {
                consoleUtils.submit(scanner, currentUser);
                return true;
            }
            case "4" -> {
                consoleUtils.viewCurrent(scanner, currentUser);
                return true;
            }
            case "5" -> {
                consoleUtils.viewIndicatorsByMonth(scanner, currentUser);
                return true;
            }
            case "6" -> {
                consoleUtils.viewHistory(currentUser);
                return true;
            }
            case "7" -> {
                return false;
            }
            default -> {
                System.out.println("\nНеизвестная команда");
                return true;
            }
        }
    }

    private boolean showMenuForAdmin(Scanner scanner) {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Сменить пользователя");
        System.out.println("3. Добавить тип показания");
        System.out.println("4. Просмотреть показания всех пользователей");
        System.out.println("5. Сделать другого пользователя администратором");
        System.out.println("6. Выход");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                currentUser = consoleUtils.register(scanner, currentUser);
                return true;
            }
            case "2" -> {
                currentUser = consoleUtils.login(scanner, currentUser);
                return true;
            }
            case "3" -> {
                consoleUtils.addIndicatorType(scanner);
                return true;
            }
            case "4" -> {
                consoleUtils.viewAllIndicators();
                return true;
            }
            case "5" -> {
                consoleUtils.makeUserAdmin(scanner);
                return true;
            }
            case "6" -> {
                return false;
            }
            default -> {
                System.out.println("Неизвестная команда");
                return true;
            }
        }
    }



}
