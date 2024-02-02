package serviceTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.*;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.services.memoryImpls.ReadingMemoryService;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Класс для тестирования сервиса показаний.
 */
public class ReadingServiceTest {
    private final ReadingType READING_TYPE = ReadingType.Create("ColdWater");

    UserRepo userRepo;
    ReadingMemoryService readingService;
    User user;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов UserRepo и ReadingService.
     */
    @BeforeEach
    public void setUp() {
        userRepo = Mockito.mock(UserRepo.class);
        readingService = new ReadingMemoryService(userRepo);
        user = User.builder().build();
    }

    @DisplayName("Тестирование метода addReading с проверкой корректного добавления показания в сервис")
    @Test
    public void addReading() {
        int value = 10;
        readingService.addReading(user, READING_TYPE, value);
        assertThat(user.getReadings()).isNotEmpty();
        assertThat(user.getReadings().get(0).getType()).isEqualTo(READING_TYPE);
    }

    @DisplayName("Тестирование метода readingExists с проверкой на существование показания")
    @Test
    public void readingExists() {
        int value = 10;
        readingService.addReading(user, READING_TYPE, value);
        assertThat(readingService.readingExists(user, READING_TYPE, LocalDate.now())).isTrue();
    }

    @DisplayName("Тестирование метода getReadingsByMonth с проверкой " +
            "корректного извлечения показаний за месяц из сервиса")
    @Test
    public void getReadingsByMonth() {
        int value = 10;
        readingService.addReading(user, READING_TYPE, value);
        List<ReadingRecord> readings = readingService.getReadingsByMonth(user, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        assertThat(readings).isNotEmpty();
    }

    @DisplayName("Тестирование метода getCurrentReading с проверкой " +
            "корректного извлечения текущего показания из сервиса")
    @Test
    public void getCurrentReading() {
        int value = 10;
        readingService.addReading(user, READING_TYPE, value);
        Optional<ReadingRecord> currentReading = readingService.getCurrentReading(user, READING_TYPE);
        assertThat(currentReading).isPresent();
    }

    @DisplayName("Тестирование метода getHistory с проверкой корректного извлечения истории показаний из сервиса")
    @Test
    public void getHistory() {
        int value = 10;
        readingService.addReading(user, READING_TYPE, value);
        List<ReadingRecord> history = readingService.getHistory(user);
        assertThat(history).isNotEmpty();
    }

    @DisplayName("Тестирование метода getAllReadings с проверкой корректного извлечения всех показаний из сервиса")
    @Test
    public void getAllReadings() {
        User user1 = User.builder()
                .accountNum("1111111111111111")
                .readings(List.of(ReadingRecord.builder().build(), ReadingRecord.builder().build()))
                .build();
        User user2 = User.builder()
                .accountNum("0000000000000000")
                .readings(List.of(ReadingRecord.builder().build()))
                .build();
        when(userRepo.getAllUsers()).thenReturn(Map.of("1111111111111111", user1, "0000000000000000", user2));
        Map<String, List<ReadingRecord>> allReadings = readingService.getAllReadings();
        assertThat(allReadings).hasSize(2);
        assertThat(allReadings.get("1111111111111111")).hasSize(2);
        assertThat(allReadings.get("0000000000000000")).hasSize(1);
    }
}
