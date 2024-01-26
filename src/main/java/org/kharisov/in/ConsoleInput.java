package org.kharisov.in;

import org.kharisov.entities.*;
import org.kharisov.enums.IndicatorTypeEnum;
import org.kharisov.in.controllers.*;
import org.kharisov.repositories.*;
import org.kharisov.services.*;
import org.kharisov.storages.*;

import java.util.*;

public class ConsoleInput {
    private final UserStorageMemory userStorage = new UserStorageMemory();

    private final UserRepo userMemoryRepo = new UserMemoryRepo(userStorage);

    private final AuthService authService = new AuthService(userMemoryRepo);
    private final IndicatorService indicatorService = new IndicatorService(userMemoryRepo);

    private final AuthController authController = new AuthController(authService);
    private final IndicatorController indicatorController = new IndicatorController(indicatorService);

    private User currentUser;

    public void start() {
        authController.addAdmin("admin", "admin");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (currentUser == null) {
                // Меню для неавторизованного пользователя
                System.out.println("\nВыберите действие:");
                System.out.println("1. Регистрация");
                System.out.println("2. Вход");
                System.out.println("3. Выход");
                String command = scanner.nextLine();
                switch (command) {
                    case "1" -> register(scanner);
                    case "2" -> login(scanner);
                    case "3" -> {
                        return;
                    }
                    default -> System.out.println("Неизвестная команда");
                }
            } else if (currentUser.isAdmin()) {
                // Меню для администратора
                System.out.println("\nВыберите действие:");
                System.out.println("1. Сменить пользователя");
                System.out.println("2. Просмотреть показания всех пользователей");
                System.out.println("3. Сделать другого пользователя администратором");
                System.out.println("4. Выход");
                String command = scanner.nextLine();
                switch (command) {
                    case "1" -> login(scanner);
                    case "2" -> viewAllIndicators();
                    case "3" -> makeUserAdmin(scanner);
                    case "4" -> {
                        return;
                    }
                    default -> System.out.println("Неизвестная команда");
                }
            } else {
                // Меню для авторизованного обычного пользователя
                System.out.println("\nВыберите действие:");
                System.out.println("1. Сменить пользователя");
                System.out.println("2. Отправить данные");
                System.out.println("3. Просмотреть текущие данные");
                System.out.println("4. Просмотреть историю");
                System.out.println("5. Выход");
                String command = scanner.nextLine();
                switch (command) {
                    case "1" -> login(scanner);
                    case "2" -> submit(scanner);
                    case "3" -> viewCurrent(scanner);
                    case "4" -> viewHistory();
                    case "5" -> {
                        return;
                    }
                    default -> System.out.println("Неизвестная команда");
                }
            }
        }
    }

    private void register(Scanner scanner) {
        System.out.println("Введите номер счета для регистрации:");
        String accountNum = scanner.nextLine();
        System.out.println("Введите пароль для регистрации:");
        String pass = scanner.nextLine();
        Optional<User> user = authController.register(accountNum, pass);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Пользователь зарегистрирован");
        } else {
            System.out.println("Пользователь уже существует");
        }
    }

    private void login(Scanner scanner) {
        System.out.println("Введите номер счета для входа:");
        String accountNum = scanner.nextLine();
        System.out.println("Введите пароль для входа:");
        String pass = scanner.nextLine();
        Optional<User> user = authController.login(accountNum, pass);
        if (user.isPresent()) {
            System.out.println("Вы вошли в систему");
            currentUser = user.get();
        } else {
            System.out.println("Неверный счет или пароль");
        }
    }

    private void makeUserAdmin(Scanner scanner) {
        System.out.println("Введите номер счета для нового админа:");
        String accountNum = scanner.nextLine();
        if (authController.makeUserAdmin(accountNum)) {
            System.out.println("Пользователь стал администратором");
        }
        else {
            System.out.println("Пользователя не существует");
        }

    }


    private void submit(Scanner scanner) {
        Optional<IndicatorTypeEnum> optionalType = selectIndicatorType(scanner);
        if (optionalType.isEmpty()) {
            System.out.println("Неверный выбор типа показания");
            return;
        }
        IndicatorTypeEnum type = optionalType.get();
        Integer value = null;
        while (value == null) {
            System.out.println("Введите показание:");
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
            } else {
                System.out.println("Введено неверное значение. Пожалуйста, введите целое число.");
                scanner.next(); // Пропустить неверный ввод
            }
        }
        scanner.nextLine(); // Чтобы очистить буфер ввода
        if (indicatorController.addIndicator(currentUser, type, value)) {
            System.out.println("Данные отправлены");
        } else {
            System.out.println("Это показание уже передано за этот месяц");
        }
    }

    private void viewCurrent(Scanner scanner) {
        Optional<IndicatorTypeEnum> optionalType = selectIndicatorType(scanner);
        if (optionalType.isEmpty()) {
            System.out.println("Неверный выбор типа показания");
            return;
        }
        IndicatorTypeEnum type = optionalType.get();
        Optional<IndicatorRecord> indicatorRecord = indicatorController.getCurrentIndicator(currentUser, type);
        if (indicatorRecord.isPresent()) {
            System.out.println(indicatorRecord);
        }
        else {
            System.out.println("Показания для данного типа не найдены");
        }

    }

    private Optional<IndicatorTypeEnum> selectIndicatorType(Scanner scanner) {
        System.out.println("Выберите показание:");
        System.out.println("1. Горячая вода");
        System.out.println("2. Холодная вода");
        System.out.println("3. Отопление");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                return Optional.of(IndicatorTypeEnum.HOTWATER);
            }
            case "2" -> {
                return Optional.of(IndicatorTypeEnum.COLDWATER);
            }
            case "3" -> {
                return Optional.of(IndicatorTypeEnum.HEATING);
            }
            default -> {
                System.out.println("Неизвестный тип показания");
                return Optional.empty();
            }
        }
    }

    private void viewHistory() {
        List<IndicatorRecord> history = indicatorController.getHistory(currentUser);
        if (!history.isEmpty()) {
            System.out.println("История показаний:");
            for (IndicatorRecord record : history) {
                System.out.println(record);
            }
        } else {
            System.out.println("История показаний пуста");
        }
    }

    private void viewAllIndicators() {
        Map<String, List<IndicatorRecord>> allIndicators = indicatorController.getAllIndicators();
        if (!allIndicators.isEmpty()) {
            System.out.println("История показаний:");
            for (Map.Entry<String, List<IndicatorRecord>> entry : allIndicators.entrySet()) {
                System.out.println("Пользователь: " + entry.getKey());
                for (IndicatorRecord record : entry.getValue()) {
                    System.out.printf("Дата: %s, Тип показания: %s, Значение: %s%n", record.getDate(), record.getType(), record.getValue());
                }
            }
        } else {
            System.out.println("История показаний пуста");
        }
    }

}
