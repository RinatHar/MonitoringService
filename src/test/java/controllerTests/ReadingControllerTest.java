package controllerTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.*;
import org.kharisov.in.controllers.ReadingController;
import org.kharisov.services.memoryImpls.ReadingMemoryService;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования контроллера чтения.
 */
public class ReadingControllerTest {

    private ReadingMemoryService readingService;
    private ReadingController readingController;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов ReadingService и ReadingController.
     */
    @BeforeEach
    public void setUp() {
        readingService = Mockito.mock(ReadingMemoryService.class);
        readingController = new ReadingController(readingService);
    }

    /**
     * Тестирование метода addReading.
     * Проверяет, что чтение корректно добавляется в сервис чтения.
     * Добавляет чтение в сервис и затем проверяет, что оно было добавлено.
     */
    @Test
    public void testAddReading() {
        User user = User.builder().build();
        ReadingType reading = ReadingType.Create("type1");
        int value = 100;

        doNothing().when(readingService).addReading(user, reading, value);

        readingController.addReading(user, reading, value);

        verify(readingService, times(1)).addReading(user, reading, value);
    }

    /**
     * Тестирование метода getCurrentIndicator.
     * Проверяет, что текущий индикатор корректно извлекается из сервиса.
     */
    @Test
    public void testGetCurrentIndicator() {
        User user = User.builder().build();
        ReadingType type = ReadingType.Create("type1");
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(100)
                .date(LocalDate.now())
                .build();

        when(readingService.getCurrentReading(user, type)).thenReturn(Optional.of(record));

        Optional<ReadingRecord> result = readingController.getCurrentIndicator(user, type);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(record);
    }

    /**
     * Тестирование метода getIndicatorsByMonth.
     * Проверяет, что индикаторы за месяц корректно извлекаются из сервиса.
     */
    @Test
    public void testGetIndicatorsByMonth() {
        User user = User.builder().build();
        int month = 1;
        int year = 2022;
        ReadingType type = ReadingType.Create("type1");
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(100)
                .date(LocalDate.of(year, month, 1))
                .build();
        List<ReadingRecord> records = Collections.singletonList(record);

        when(readingService.getReadingsByMonth(user, month, year)).thenReturn(records);

        List<ReadingRecord> result = readingController.getIndicatorsByMonth(user, month, year);

        assertThat(result).isEqualTo(records);
    }

    /**
     * Тестирование метода getHistory.
     * Проверяет, что история чтений корректно извлекается из сервиса.
     */
    @Test
    public void testGetHistory() {
        User user = User.builder().build();
        ReadingType type = ReadingType.Create("type1");
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(100)
                .date(LocalDate.now())
                .build();
        List<ReadingRecord> records = Collections.singletonList(record);

        when(readingService.getHistory(user)).thenReturn(records);

        List<ReadingRecord> result = readingController.getHistory(user);

        assertThat(result).isEqualTo(records);
    }

    /**
     * Тестирование метода getAllReadings.
     * Проверяет, что все чтения корректно извлекаются из сервиса.
     */
    @Test
    public void testGetAllReadings() {
        User user = User.builder().build();
        ReadingType type = ReadingType.Create("type1");
        ReadingRecord record = ReadingRecord.builder()
                .type(type)
                .value(100)
                .date(LocalDate.now())
                .build();
        List<ReadingRecord> records = Collections.singletonList(record);
        Map<String, List<ReadingRecord>> allReadings = new HashMap<>();
        allReadings.put(user.getAccountNum(), records);

        when(readingService.getAllReadings()).thenReturn(allReadings);

        Map<String, List<ReadingRecord>> result = readingController.getAllReadings();

        assertThat(result).isEqualTo(allReadings);
    }

    /**
     * Тестирование метода readingExists.
     * Проверяет, существует ли чтение.
     */
    @Test
    public void testReadingExists() {
        User user = User.builder().build();
        ReadingType reading = ReadingType.Create("type1");
        LocalDate now = LocalDate.now();

        when(readingService.readingExists(user, reading, now)).thenReturn(true);

        boolean result = readingController.readingExists(user, reading, now);

        assertThat(result).isTrue();
    }
}

