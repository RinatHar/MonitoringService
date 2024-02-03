package serviceTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.ReadingType;
import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.services.memoryImpls.ReadingTypeMemoryService;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования сервиса типов показаний.
 */
public class ReadingTypeServiceTest {
    private final String READING_TYPE = "ColdWater";

    ReadingTypeRepo readingTypeRepo;
    ReadingTypeMemoryService readingTypeService;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов ReadingTypeRepo и ReadingTypeService.
     */
    @BeforeEach
    public void setUp() {
        readingTypeRepo = Mockito.mock(ReadingTypeRepo.class);
        readingTypeService = new ReadingTypeMemoryService(readingTypeRepo);
    }

    @DisplayName("Тестирование метода addOnlyOneReadingType с проверкой, " +
            "что тип показания добавляется только один раз")
    @Test
    public void addOnlyOneReadingType() {
        doNothing().when(readingTypeRepo).addReadingType(READING_TYPE);
        readingTypeService.addReadingType(READING_TYPE);
        verify(readingTypeRepo, times(1)).addReadingType(READING_TYPE);
    }

    @DisplayName("Тестирование метода getReadingType с проверкой корректного извлечения типа показания из сервиса")
    @Test
    public void getReadingType() {
        ReadingType readingType = ReadingType.Create(READING_TYPE);
        when(readingTypeRepo.getReadingType(READING_TYPE)).thenReturn(Optional.of(readingType));
        Optional<ReadingType> result = readingTypeService.getReadingType(READING_TYPE);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(readingType);
    }

    @DisplayName("Тестирование метода getReadingNames с проверкой " +
            "корректного извлечения имен типов показания из сервиса")
    @Test
    public void getReadingNames() {
        Set<String> names = Set.of("ColdWater", "HotWater");
        when(readingTypeRepo.getReadingNames()).thenReturn(names);
        Set<String> result = readingTypeService.getReadingNames();
        assertThat(result).isEqualTo(names);
    }
}
