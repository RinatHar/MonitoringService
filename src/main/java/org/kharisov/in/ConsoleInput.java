package org.kharisov.in;

import org.kharisov.entities.*;
import org.kharisov.in.controllers.*;

import java.util.*;

/**
 * Класс ConsoleInput представляет собой точку входа для взаимодействия пользователя с консолью.
 * Этот класс предоставляет методы для инициализации администратора и типов показателей,
 * отображения меню для администратора,неаутентифицированных и аутентифицированных пользователей,
 * а также для запуска взаимодействия с консолью.
 */
public class ConsoleInput {

    /**
     * Контроллер для работы с аутентификацией пользователей.
     */
    private final AuthController authController;

    /**
     * Контроллер для работы с показаниями чтения.
     */
    private final ReadingController readingController;

    /**
     * Контроллер для работы с типами показаний чтения.
     */
    private final ReadingTypeController readingTypeController;

    /**
     * Контроллер для работы с аудитом.
     */
    private final AuditController auditController;

    /**
     * Утилита для взаимодействия с пользователем через консоль.
     */
    private final ConsoleUtils consoleUtils;
    /**
     * Текущий пользователь.
     */
    private User currentUser;

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Конструктор для класса ConsoleInput
     */
    public ConsoleInput(AuthController authController,
                        ReadingController readingController,
                        ReadingTypeController readingTypeController,
                        AuditController auditController,
                        ConsoleUtils consoleUtils) {
        this.authController = authController;
        this.readingController = readingController;
        this.readingTypeController = readingTypeController;
        this.auditController = auditController;
        this.consoleUtils = consoleUtils;
    }

    /**
     * Запускает взаимодействие пользователя с консолью.
     */
    public void start() {
        initAdminAndIndicatorTypes();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            if (getCurrentUser() == null) {
                running = showMenuForUnauthenticatedUser(scanner);
            } else if (getCurrentUser().isAdmin()) {
                running = showMenuForAdmin(scanner);
            } else {
                running = showMenuForAuthenticatedUser(scanner);
            }
        }
    }

    /**
     * Инициализирует администратора и типы показателей.
     */
    public void initAdminAndIndicatorTypes() {
        authController.addAdmin("0000000000000000", "admin12345");
        readingTypeController.addReadingType("Горячая вода");
        readingTypeController.addReadingType("Холодная вода");
        readingTypeController.addReadingType("Отопление");
    }

    /**
     * Отображает меню для неаутентифицированного пользователя.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @return true, если взаимодействие с консолью продолжается, иначе false.
     */
    public boolean showMenuForUnauthenticatedUser(Scanner scanner) {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("3. Выход");
        System.out.print("> ");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                setCurrentUser(consoleUtils.register(scanner, currentUser));
                return true;
            }
            case "2" -> {
                setCurrentUser(consoleUtils.login(scanner, currentUser));
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

    /**
     * Отображает меню для аутентифицированного пользователя.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @return true, если взаимодействие с консолью продолжается, иначе false.
     */
    public boolean showMenuForAuthenticatedUser(Scanner scanner) {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Сменить пользователя");
        System.out.println("3. Отправить данные");
        System.out.println("4. Просмотреть текущие данные");
        System.out.println("5. Просмотреть данные за месяц");
        System.out.println("6. Просмотреть историю");
        System.out.println("7. Выход");
        System.out.print("> ");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                auditController.logAction(currentUser, "Регистрация");
                setCurrentUser(consoleUtils.register(scanner, currentUser));
                return true;
            }
            case "2" -> {
                auditController.logAction(currentUser, "Сменить пользователя");
                setCurrentUser(consoleUtils.login(scanner, currentUser));
                return true;
            }
            case "3" -> {
                auditController.logAction(currentUser, "Отправить данные");
                consoleUtils.submit(scanner, currentUser);
                return true;
            }
            case "4" -> {
                auditController.logAction(currentUser, "Просмотреть текущие данные");
                consoleUtils.viewCurrent(scanner, currentUser);
                return true;
            }
            case "5" -> {
                auditController.logAction(currentUser, "Просмотреть историю");
                consoleUtils.viewIndicatorsByMonth(scanner, currentUser);
                return true;
            }
            case "6" -> {
                auditController.logAction(currentUser, "Просмотреть историю");
                consoleUtils.viewHistory(currentUser);
                return true;
            }
            case "7" -> {
                auditController.logAction(currentUser, "Выход");
                return false;
            }
            default -> {
                System.out.println("\nНеизвестная команда");
                return true;
            }
        }
    }

    /**
     * Отображает меню для администратора.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @return true, если взаимодействие с консолью продолжается, иначе false.
     */
    private boolean showMenuForAdmin(Scanner scanner) {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Сменить пользователя");
        System.out.println("3. Отправить данные");
        System.out.println("4. Просмотреть текущие данные");
        System.out.println("5. Просмотреть данные за месяц");
        System.out.println("6. Просмотреть историю");
        System.out.println("7. Добавить тип показания");
        System.out.println("8. Просмотреть показания всех пользователей");
        System.out.println("9. Сделать другого пользователя администратором");
        System.out.println("10. Просмотр действий всех пользователей");
        System.out.println("11. Выход");
        System.out.print("> ");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                auditController.logAction(currentUser, "Регистрация");
                setCurrentUser(consoleUtils.register(scanner, currentUser));
                return true;
            }
            case "2" -> {
                auditController.logAction(currentUser, "Сменить пользователя");
                setCurrentUser(consoleUtils.login(scanner, currentUser));
                return true;
            }
            case "3" -> {
                auditController.logAction(currentUser, "Отправить данные");
                consoleUtils.submit(scanner, currentUser);
                return true;
            }
            case "4" -> {
                auditController.logAction(currentUser, "Просмотреть текущие данные");
                consoleUtils.viewCurrent(scanner, currentUser);
                return true;
            }
            case "5" -> {
                auditController.logAction(currentUser, "Просмотреть данные за месяц");
                consoleUtils.viewIndicatorsByMonth(scanner, currentUser);
                return true;
            }
            case "6" -> {
                auditController.logAction(currentUser, "Просмотреть историю");
                consoleUtils.viewHistory(currentUser);
                return true;
            }
            case "7" -> {
                auditController.logAction(currentUser, "Добавить тип показания");
                consoleUtils.addIndicatorType(scanner);
                return true;
            }
            case "8" -> {
                auditController.logAction(currentUser, "Просмотреть показания всех пользователей");
                consoleUtils.viewAllReadings();
                return true;
            }
            case "9" -> {
                auditController.logAction(currentUser, "Сделать другого пользователя администратором");
                consoleUtils.makeUserAdmin(scanner);
                return true;
            }
            case "10" -> {
                auditController.logAction(currentUser, "Просмотр действий всех пользователей");
                consoleUtils.getAllLogs();
                return true;
            }
            case "11" -> {
                auditController.logAction(currentUser, "Выход");
                return false;
            }
            default -> {
                System.out.println("Неизвестная команда");
                return true;
            }
        }
    }
}
