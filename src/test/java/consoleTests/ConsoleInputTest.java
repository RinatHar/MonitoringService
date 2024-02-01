package consoleTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.in.*;
import org.kharisov.in.controllers.*;
import org.mockito.Mockito;

import java.io.*;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования ввода с консоли.
 */
public class ConsoleInputTest {
    private AuthController authController;
    private ReadingController readingController;
    private ReadingTypeController readingTypeController;
    private AuditController auditController;
    private ConsoleUtils consoleUtils;
    private ConsoleInput consoleInput;

    /**
     * Настройка перед каждым тестом.
     */
    @BeforeEach
    public void setup() {
        authController = Mockito.mock(AuthController.class);
        readingController = Mockito.mock(ReadingController.class);
        readingTypeController = Mockito.mock(ReadingTypeController.class);
        auditController = Mockito.mock(AuditController.class);
        consoleUtils = Mockito.mock(ConsoleUtils.class);
        consoleInput = new ConsoleInput(authController, readingController, readingTypeController, auditController, consoleUtils);
    }

    /**
     * Тестирование метода start.
     * Пользователь выбирает первую опцию в меню, а затем выбирает '3' для выхода.
     */
    @Test
    public void testStart() {
        String input = "1\n3\n"; // Пользователь выбирает первую опцию в меню, а затем выбирает '3' для выхода
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        consoleInput.start();
        verify(consoleUtils, times(1)).register(any(Scanner.class), isNull());
    }

    /**
     * Тестирование метода initAdminAndIndicatorTypes.
     * Проверяет добавление администратора и типов показателей.
     */
    @Test
    public void testInitAdminAndIndicatorTypes() {
        consoleInput.initAdminAndIndicatorTypes();
        verify(authController, times(1)).addAdmin("0000000000000000", "admin12345");
        verify(readingTypeController, times(1)).addReadingType("Горячая вода");
        verify(readingTypeController, times(1)).addReadingType("Холодная вода");
        verify(readingTypeController, times(1)).addReadingType("Отопление");
    }

    /**
     * Тестирование метода showMenuForUnauthenticatedUser.
     * Пользователь выбирает первую опцию в меню.
     */
    @Test
    public void testShowMenuForUnauthenticatedUser() {
        String input = "1\n"; // Пользователь выбирает первую опцию в меню
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        consoleInput.showMenuForUnauthenticatedUser(new Scanner(in));
        verify(consoleUtils, times(1)).register(any(Scanner.class), isNull());
    }

    /**
     * Тестирование метода showMenuForAuthenticatedUser.
     * Пользователь выбирает первую опцию в меню.
     */
    @Test
    public void testShowMenuForAuthenticatedUser() {
        consoleInput.setCurrentUser(User.builder().build());
        String input = "1\n"; // Пользователь выбирает первую опцию в меню
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        consoleInput.showMenuForAuthenticatedUser(new Scanner(in));
        verify(auditController, times(1)).addEntry(any(User.class), any(String.class));
        verify(consoleUtils, times(1)).register(any(Scanner.class), any(User.class));
    }
}

