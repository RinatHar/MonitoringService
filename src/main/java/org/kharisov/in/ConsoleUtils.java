package org.kharisov.in;

import org.kharisov.entities.*;
import org.kharisov.in.controllers.*;
import org.kharisov.storages.IndicatorType;

import java.time.LocalDate;
import java.util.*;

public class ConsoleUtils {
    private final AuthController authController;
    private final IndicatorController indicatorController;
    private final IndicatorTypeController indicatorTypeController;
    private final AuditController auditController;

    public ConsoleUtils(AuthController authController,
                        IndicatorController indicatorController,
                        IndicatorTypeController indicatorTypeController,
                        AuditController auditController) {
        this.authController = authController;
        this.indicatorController = indicatorController;
        this.indicatorTypeController = indicatorTypeController;
        this.auditController = auditController;
    }

    public User register(Scanner scanner, User currentUser) {
        System.out.println("\nВведите номер счета для регистрации:");
        String accountNum = scanner.nextLine();
        System.out.println("Введите пароль для регистрации:");
        String pass = scanner.nextLine();
        Optional<User> user = authController.register(accountNum, pass);
        if (user.isPresent()) {
            System.out.println("\nПользователь зарегистрирован");
            return user.get();
        } else {
            System.out.println("\nНомер счета должен состоять из 16 целых чисел и пароль не менее 8 символов");
            return currentUser;
        }
    }

    public User login(Scanner scanner, User currentUser) {
        System.out.println("\nВведите номер счета для входа:");
        String accountNum = scanner.nextLine();
        System.out.println("Введите пароль для входа:");
        String pass = scanner.nextLine();
        Optional<User> user = authController.login(accountNum, pass);
        if (user.isPresent()) {
            System.out.println("\nВы вошли в систему");
            return user.get();
        } else {
            System.out.println("\nНеверный счет или пароль");
            return currentUser;
        }
    }

    public void makeUserAdmin(Scanner scanner) {
        System.out.println("\nВведите номер счета для нового админа:");
        String accountNum = scanner.nextLine();
        if (authController.makeUserAdmin(accountNum)) {
            System.out.println("\nПользователь стал администратором");
        }
        else {
            System.out.println("\nПользователя не существует");
        }

    }


    public void submit(Scanner scanner, User currentUser) {
        Optional<IndicatorType> optionalType = selectIndicatorType(scanner);
        if (optionalType.isEmpty()) {
            System.out.println("\nНеверный выбор типа показания");
            return;
        }
        IndicatorType type = optionalType.get();
        if (indicatorController.indicatorExists(currentUser, type, LocalDate.now())) {
            System.out.println("\nЭто показание уже передано за этот месяц");
            return;
        }
        Integer value = null;
        while (value == null) {
            System.out.println("\nВведите показание:");
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
            } else {
                System.out.println("\nВведено неверное значение. Пожалуйста, введите целое число.");
                scanner.next(); // Пропустить неверный ввод
            }
        }
        scanner.nextLine(); // Чтобы очистить буфер ввода
        indicatorController.addIndicator(currentUser, type, value);
        System.out.println("\nДанные отправлены");

    }

    public void addIndicatorType(Scanner scanner) {
        System.out.println("\nВведите новый тип показания:");
        String indicatorType = scanner.nextLine();
        indicatorTypeController.addIndicatorType(indicatorType);
        System.out.println("\nНовый тип показания добавлен");

    }

    public void viewCurrent(Scanner scanner, User currentUser) {
        Optional<IndicatorType> optionalType = selectIndicatorType(scanner);
        if (optionalType.isEmpty()) {
            System.out.println("\nНеверный выбор типа показания");
            return;
        }
        IndicatorType type = optionalType.get();
        Optional<IndicatorRecord> indicatorRecord = indicatorController.getCurrentIndicator(currentUser, type);
        if (indicatorRecord.isPresent()) {
            System.out.println(indicatorRecord);
        }
        else {
            System.out.println("\nПоказания для данного типа не найдены");
        }
    }

    public void viewIndicatorsByMonth(Scanner scanner, User currentUser) {
        int month;
        int year;
        try {
            System.out.println("Введите год:");
            year = inputInt(scanner);
            System.out.println("Введите месяц:");
            month = inputInt(scanner);
            scanner.nextLine();
        } catch (InputMismatchException ex) {
            System.out.println("\nМесяц и год должны быть числами");
            scanner.nextLine();
            return;
        }
        if (!checkMonthAndYear(month, year))
            return;
        List<IndicatorRecord> indicatorRecords = indicatorController.getIndicatorsByMonth(currentUser, month, year);
        if (!indicatorRecords.isEmpty()) {
            for (IndicatorRecord record : indicatorRecords) {
                System.out.println(record);
            }
        }
        else {
            System.out.println("\nПоказания за этот месяц и год не найдены");
        }
    }

    private int inputInt(Scanner scanner) throws InputMismatchException {
        return scanner.nextInt();
    }

    private boolean checkMonthAndYear(int month, int year) {
        int MIN_YEAR = 2000;
        int MAX_YEAR = LocalDate.now().getYear();
        if (month < 1 || month > 12) {
            System.out.println("\nНекорректный месяц. Месяц должен быть числом от 1 до 12");
            return false;
        }
        if (year < MIN_YEAR || year > MAX_YEAR) {
            System.out.println("\nНекорректный год. Год должен быть от " + MIN_YEAR + " до " + MAX_YEAR);
            return false;
        }
        return true;
    }

    public Optional<IndicatorType> selectIndicatorType(Scanner scanner) {
        System.out.println("\nВыберите показание:");
        List<String> indicatorNames = new ArrayList<>(indicatorTypeController.getIndicatorNames());
        for (int i = 0; i < indicatorNames.size(); i++) {
            System.out.println((i + 1) + ". " + indicatorNames.get(i));
        }
        String input = scanner.nextLine();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < indicatorNames.size()) {
                return indicatorTypeController.getIndicatorType(indicatorNames.get(index));
            } else {
                System.out.println("\nНеизвестный тип показания");
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void viewHistory(User currentUser) {
        List<IndicatorRecord> history = indicatorController.getHistory(currentUser);
        if (!history.isEmpty()) {
            System.out.println("\nИстория показаний:");
            for (IndicatorRecord record : history) {
                System.out.println(record);
            }
        } else {
            System.out.println("\nИстория показаний пуста");
        }
    }

    public void viewAllIndicators() {
        Map<String, List<IndicatorRecord>> allIndicators = indicatorController.getAllIndicators();
        if (!allIndicators.isEmpty()) {
            System.out.println("\nИстория показаний:");
            for (Map.Entry<String, List<IndicatorRecord>> entry : allIndicators.entrySet()) {
                System.out.println("\nПользователь: " + entry.getKey());
                for (IndicatorRecord record : entry.getValue()) {
                    System.out.printf("Дата: %s, Тип показания: %s, Значение: %s%n", record.getDate(), record.getType().getValue(), record.getValue());
                }
            }
        } else {
            System.out.println("\nИстория показаний пуста");
        }
    }

    public void getUserLogs(User user) {
        List<String> logs = auditController.getLogs(user);
        if (!logs.isEmpty()) {
            System.out.println("\nВаши действия:");
            for (String log : logs) {
                System.out.println(" - " + log);
            }
        } else {
            System.out.println("\nДействий не было");
        }

    }

    public void getAllLogs() {
        Map<String, List<String>> logs = auditController.getAllLogs();
        for (Map.Entry<String, List<String>> entry : logs.entrySet()) {
            System.out.println("\nПользователь: " + entry.getKey());
            for (String log : entry.getValue()) {
                System.out.println(log);
            }
            System.out.println("--------------------");
        }
    }
}
