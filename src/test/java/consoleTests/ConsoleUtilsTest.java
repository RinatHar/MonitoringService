package consoleTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.*;
import org.kharisov.in.ConsoleUtils;
import org.kharisov.in.controllers.*;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования утилит консоли.
 */
public class ConsoleUtilsTest {
    private final String ACCOUNT_NUM = "1234567890123456";
    private final String PASSWORD = "password";
    private final String READING_TYPE = "ColdWater";
    private final int READING_VALUE = 100;
    private final LocalDate DATE_NOW = LocalDate.now();

    private AuthController authController;
    private ReadingController readingController;
    private ReadingTypeController readingTypeController;
    private AuditController auditController;
    private ConsoleUtils consoleUtils;

    /**
     * Настройка перед каждым тестом.
     */
    @BeforeEach
    public void setup() {
        authController = Mockito.mock(AuthController.class);
        readingController = Mockito.mock(ReadingController.class);
        readingTypeController = Mockito.mock(ReadingTypeController.class);
        auditController = Mockito.mock(AuditController.class);
        consoleUtils = new ConsoleUtils(authController, readingController, readingTypeController, auditController);
    }

    @DisplayName("Тестирование метода register с проверкой регистрации пользователя")
    @Test
    public void testRegister() {
        User user = User.builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
        Scanner scanner = new Scanner(ACCOUNT_NUM + "\n" + PASSWORD + "\n");
        when(authController.register(ACCOUNT_NUM, PASSWORD)).thenReturn(Optional.of(user));
        assertThat(consoleUtils.register(scanner, null)).isEqualTo(user);
    }

    @DisplayName("Тестирование метода login с проверкой входа пользователя в систему")
    @Test
    public void testLogin() {
        User user = User.builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
        Scanner scanner = new Scanner(ACCOUNT_NUM + "\n" + PASSWORD + "\n");
        when(authController.login(ACCOUNT_NUM, PASSWORD)).thenReturn(Optional.of(user));
        assertThat(consoleUtils.login(scanner, null)).isEqualTo(user);
    }

    @DisplayName("Тестирование метода makeUserAdmin с проверкой назначения пользователя администратором")
    @Test
    public void testMakeUserAdmin() {
        Scanner scanner = new Scanner(ACCOUNT_NUM + "\n");
        when(authController.makeUserAdmin(ACCOUNT_NUM)).thenReturn(true);
        consoleUtils.makeUserAdmin(scanner);
        verify(authController, times(1)).makeUserAdmin(ACCOUNT_NUM);
    }

    @DisplayName("Тестирование метода submit с проверкой отправки данных пользователем")
    @Test
    public void testSubmit() {
        User user = User.builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
        ReadingType type = ReadingType.Create(READING_TYPE);
        Set<String> readingNames = Set.of(READING_TYPE);
        Scanner scanner = new Scanner("1\n" + READING_VALUE + "\n");
        when(readingTypeController.getReadingNames()).thenReturn(readingNames);
        when(readingTypeController.getReadingType(READING_TYPE)).thenReturn(Optional.of(type));
        when(readingController.readingExists(user, type, DATE_NOW)).thenReturn(false);
        consoleUtils.submit(scanner, user);
        verify(readingController, times(1)).addReading(user, type, READING_VALUE);
    }

    @DisplayName("Тестирование метода addIndicatorType с проверкой добавления типа индикатора")
    @Test
    public void testAddIndicatorType() {
        Scanner scanner = new Scanner(READING_TYPE + "\n");
        consoleUtils.addIndicatorType(scanner);
        verify(readingTypeController, times(1)).addReadingType(READING_TYPE);
    }

    @DisplayName("Тестирование метода viewCurrent с проверкой просмотра текущих показателей пользователем")
    @Test
    public void testViewCurrent() {
        User user = User.builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
        ReadingType type = ReadingType.Create(READING_TYPE);
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(READING_VALUE)
                .date(DATE_NOW)
                .build();
        Set<String> readingNames = Set.of(READING_TYPE);
        Scanner scanner = new Scanner("1\n");
        when(readingTypeController.getReadingNames()).thenReturn(readingNames);
        when(readingTypeController.getReadingType(READING_TYPE)).thenReturn(Optional.of(type));
        when(readingController.getCurrentIndicator(user, type)).thenReturn(Optional.of(record));
        consoleUtils.viewCurrent(scanner, user);
        verify(readingController, times(1)).getCurrentIndicator(user, type);
    }

    @DisplayName("Тестирование метода viewIndicatorsByMonth с проверкой просмотра показателей пользователем по месяцам")
    @Test
    public void testViewIndicatorsByMonth() {
        User user = User.builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
        ReadingType type = ReadingType.Create(READING_TYPE);
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(READING_VALUE)
                .date(DATE_NOW)
                .build();
        List<ReadingRecord> records = Collections.singletonList(record);
        Scanner scanner = new Scanner(DATE_NOW.getYear() + "\n" + DATE_NOW.getMonthValue() + "\n");
        when(readingController.getIndicatorsByMonth(user, DATE_NOW.getMonthValue(), DATE_NOW.getYear())).thenReturn(records);
        consoleUtils.viewIndicatorsByMonth(scanner, user);
        verify(readingController, times(1)).getIndicatorsByMonth(user, DATE_NOW.getMonthValue(), DATE_NOW.getYear());
    }

    @DisplayName("Тестирование метода selectIndicatorType с проверкой выбора типа индикатора")
    @Test
    public void testSelectIndicatorType() {
        ReadingType type = ReadingType.Create(READING_TYPE);
        Set<String> readingNames = Set.of(READING_TYPE);
        Scanner scanner = new Scanner("1\n");
        when(readingTypeController.getReadingNames()).thenReturn(readingNames);
        when(readingTypeController.getReadingType(READING_TYPE)).thenReturn(Optional.of(type));
        assertThat(consoleUtils.selectIndicatorType(scanner)).isEqualTo(Optional.of(type));
    }

    @DisplayName("Тестирование метода viewHistory с проверкой просмотра истории пользователем")
    @Test
    public void testViewHistory() {
        User user = User.builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
        ReadingType type = ReadingType.Create(READING_TYPE);
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(READING_VALUE)
                .date(DATE_NOW)
                .build();
        List<ReadingRecord> records = Collections.singletonList(record);
        when(readingController.getHistory(user)).thenReturn(records);
        consoleUtils.viewHistory(user);
        verify(readingController, times(1)).getHistory(user);
    }

    @DisplayName("Тестирование метода viewAllReadings с проверкой просмотра всех показателей")
    @Test
    public void testViewAllReadings() {
        ReadingType type = ReadingType.Create(READING_TYPE);
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(READING_VALUE)
                .date(DATE_NOW)
                .build();
        Map<String, List<ReadingRecord>> allReadings = new HashMap<>();
        allReadings.put(ACCOUNT_NUM, Collections.singletonList(record));
        when(readingController.getAllReadings()).thenReturn(allReadings);
        consoleUtils.viewAllReadings();
        verify(readingController, times(1)).getAllReadings();
    }

    @DisplayName("Тестирование метода getAllEntries с проверкой получения всех журналов")
    @Test
    public void testGetAllEntries() {
        List<String> entries = Collections.singletonList("entry1");
        Map<String, List<String>> allEntries = new HashMap<>();
        allEntries.put(ACCOUNT_NUM, entries);
        when(auditController.getAllEntries()).thenReturn(allEntries);
        when(authController.isAdminByAccountNum(ACCOUNT_NUM)).thenReturn(true);
        consoleUtils.getAllEntries();
        verify(auditController, times(1)).getAllEntries();
    }



}
