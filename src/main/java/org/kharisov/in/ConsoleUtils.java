package org.kharisov.in;

import org.kharisov.entities.*;
import org.kharisov.in.controllers.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс ConsoleUtils представляет утилиту для взаимодействия с пользователем через консоль.
 * Этот класс предоставляет методы для регистрации, входа в систему, назначения пользователя администратором,
 * отправки показаний чтения, добавления нового типа показания, просмотра текущих показаний,
 * просмотра показателей по месяцам, выбора типа показателя, просмотра истории показателей,
 * просмотра всех показателей и получения всех журналов аудита.
 */
public class ConsoleUtils {
    /**
     * Контроллер для работы с аутентификацией.
     */
    private final AuthController authController;

    /**
     * Контроллер для работы с показаниями.
     */
    private final ReadingController readingController;

    /**
     * Контроллер для работы с типами показаний.
     */
    private final ReadingTypeController readingTypeController;

    /**
     * Контроллер для работы с аудитом.
     */
    private final AuditController auditController;

    /**
     * Конструктор класса ConsoleUtils.
     * @param authController Контроллер для работы с аутентификацией.
     * @param readingController Контроллер для работы с показаниями.
     * @param readingTypeController Контроллер для работы с типами показаний.
     * @param auditController Контроллер для работы с аудитом.
     */
    public ConsoleUtils(AuthController authController,
                        ReadingController readingController,
                        ReadingTypeController readingTypeController,
                        AuditController auditController) {
        this.authController = authController;
        this.readingController = readingController;
        this.readingTypeController = readingTypeController;
        this.auditController = auditController;
    }

    /**
     * Регистрирует нового пользователя с указанным номером счета и паролем.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param currentUser Текущий пользователь.
     * @return Объект User, представляющий зарегистрированного пользователя.
     */
    public User register(Scanner scanner, User currentUser) {
        System.out.println("\nВведите номер счета для регистрации:");
        System.out.print("> ");
        String accountNum = scanner.nextLine();
        System.out.println("Введите пароль для регистрации:");
        System.out.print("> ");
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

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param currentUser Текущий пользователь.
     * @return Объект User, представляющий пользователя, вошедшего в систему.
     */
    public User login(Scanner scanner, User currentUser) {
        System.out.println("\nВведите номер счета для входа:");
        System.out.print("> ");
        String accountNum = scanner.nextLine();
        System.out.println("Введите пароль для входа:");
        System.out.print("> ");
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

    /**
     * Назначает пользователя администратором.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     */
    public void makeUserAdmin(Scanner scanner) {
        System.out.println("\nВведите номер счета для нового админа:");
        System.out.print("> ");
        String accountNum = scanner.nextLine();
        if (authController.makeUserAdmin(accountNum)) {
            System.out.println("\nПользователь стал администратором");
        }
        else {
            System.out.println("\nПользователя не существует");
        }

    }

    /**
     * Отправляет показание для указанного пользователя.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param currentUser Текущий пользователь.
     */
    public void submit(Scanner scanner, User currentUser) {
        Optional<ReadingType> optionalType = selectIndicatorType(scanner);
        if (optionalType.isEmpty()) {
            System.out.println("\nНеверный выбор типа показания");
            return;
        }
        ReadingType type = optionalType.get();
        if (readingController.readingExists(currentUser, type, LocalDate.now())) {
            System.out.println("\nЭто показание уже передано за этот месяц");
            return;
        }
        Integer value = null;
        while (value == null) {
            System.out.println("\nВведите показание:");
            System.out.print("> ");
            if (scanner.hasNextInt()) {
                int inputValue = scanner.nextInt();
                if (inputValue < 0) {
                    System.out.println("\nВведено отрицательное число. Пожалуйста, введите положительное целое число.");
                } else {
                    value = inputValue;
                }
            } else {
                System.out.println("\nВведено неверное значение. Пожалуйста, введите целое число.");
                scanner.next(); // Пропустить неверный ввод
            }
        }
        scanner.nextLine(); // Чтобы очистить буфер ввода
        readingController.addReading(currentUser, type, value);
        System.out.println("\nДанные отправлены");

    }

    /**
     * Добавляет новый тип показания.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     */
    public void addIndicatorType(Scanner scanner) {
        System.out.println("\nВведите новый тип показания:");
        System.out.print("> ");
        String indicatorType = scanner.nextLine();
        readingTypeController.addReadingType(indicatorType);
        System.out.println("\nНовый тип показания добавлен");

    }

    /**
     * Просматривает текущие показания указанного пользователя.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param currentUser Текущий пользователь.
     */
    public void viewCurrent(Scanner scanner, User currentUser) {
        Optional<ReadingType> optionalType = selectIndicatorType(scanner);
        if (optionalType.isEmpty()) {
            System.out.println("\nНеверный выбор типа показания");
            return;
        }
        ReadingType type = optionalType.get();
        Optional<ReadingRecord> indicatorRecord = readingController.getCurrentIndicator(currentUser, type);
        if (indicatorRecord.isPresent()) {
            System.out.println(indicatorRecord.get());
        }
        else {
            System.out.println("\nПоказания для данного типа не найдены");
        }
    }

    /**
     * Просматривает показатели указанного пользователя за указанный месяц и год.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param currentUser Текущий пользователь.
     */
    public void viewIndicatorsByMonth(Scanner scanner, User currentUser) {
        int month;
        int year;
        try {
            System.out.println("Введите год:");
            System.out.print("> ");
            year = inputInt(scanner);
            System.out.println("Введите месяц:");
            System.out.print("> ");
            month = inputInt(scanner);
            scanner.nextLine();
        } catch (InputMismatchException ex) {
            System.out.println("\nМесяц и год должны быть числами");
            scanner.nextLine();
            return;
        }
        if (!checkMonthAndYear(month, year))
            return;
        List<ReadingRecord> readingRecords = readingController.getIndicatorsByMonth(currentUser, month, year);
        if (!readingRecords.isEmpty()) {
            for (ReadingRecord record : readingRecords) {
                System.out.println(record);
            }
        }
        else {
            System.out.println("\nПоказания за этот месяц и год не найдены");
        }
    }

    /**
     * Вводит целое число из ввода пользователя.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @return Введенное пользователем целое число.
     * @throws InputMismatchException Если введенное значение не является целым числом.
     */
    private int inputInt(Scanner scanner) throws InputMismatchException {
        return scanner.nextInt();
    }

    /**
     * Проверяет, являются ли указанный месяц и год допустимыми.
     * @param month Месяц для проверки.
     * @param year Год для проверки.
     * @return true, если месяц и год допустимы, иначе false.
     */
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

    /**
     * Выбирает тип показателя из ввода пользователя.
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @return Optional<ReadingType>, содержащий выбранный тип показателя, если выбор допустим, иначе Optional.empty().
     */
    public Optional<ReadingType> selectIndicatorType(Scanner scanner) {
        System.out.println("\nВыберите показание:");
        List<String> readingNames = new ArrayList<>(readingTypeController.getReadingNames());
        for (int i = 0; i < readingNames.size(); i++) {
            System.out.println((i + 1) + ". " + readingNames.get(i));
        }
        System.out.print("> ");
        String input = scanner.nextLine();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < readingNames.size()) {
                return readingTypeController.getReadingType(readingNames.get(index));
            } else {
                System.out.println("\nНеизвестный тип показания");
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Просматривает историю показателей указанного пользователя.
     * @param currentUser Текущий пользователь.
     */
    public void viewHistory(User currentUser) {
        List<ReadingRecord> history = readingController.getHistory(currentUser);
        if (!history.isEmpty()) {
            System.out.println("\nИстория показаний:");
            for (ReadingRecord record : history) {
                System.out.println(record);
            }
        } else {
            System.out.println("\nИстория показаний пуста");
        }
    }

    /**
     * Просматривает все показатели всех пользователей.
     */
    public void viewAllReadings() {
        Map<String, List<ReadingRecord>> allReadings = readingController.getAllReadings();
        if (!allReadings.isEmpty()) {
            System.out.println("\nИстория показаний:");
            for (Map.Entry<String, List<ReadingRecord>> entry : allReadings.entrySet()) {
                System.out.println("\nПользователь: " + entry.getKey());
                for (ReadingRecord record : entry.getValue()) {
                    System.out.printf("Дата: %s, Тип показания: %s, Значение: %s%n", record.getDate(), record.getType().getValue(), record.getValue());
                }
            }
        } else {
            System.out.println("\nИстория показаний пуста");
        }
    }

    /**
     * Получает все журналы аудита.
     */
    public void getAllEntries() {
        Map<String, List<String>> entries = auditController.getAllEntries();
        for (Map.Entry<String, List<String>> entry : entries.entrySet()) {
            System.out.println("\nПользователь: " + entry.getKey() + (authController.isAdminByAccountNum(entry.getKey()) ? " (Администратор)" : ""));
            for (String value : entry.getValue()) {
                System.out.println(" - " + value);
            }
            System.out.println("--------------------");
        }
    }
}
