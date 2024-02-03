package controllerTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.ReadingType;
import org.kharisov.in.controllers.ReadingTypeController;
import org.kharisov.services.memoryImpls.ReadingTypeMemoryService;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования контроллера типов чтения.
 */
public class ReadingTypeControllerTest {

    private ReadingTypeMemoryService readingTypeService;
    private ReadingTypeController readingTypeController;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов ReadingTypeService и ReadingTypeController.
     */
    @BeforeEach
    public void setUp() {
        readingTypeService = Mockito.mock(ReadingTypeMemoryService.class);
        readingTypeController = new ReadingTypeController(readingTypeService);
    }

    @DisplayName("Тестирование метода addReadingType с проверкой корректного добавления типа чтения в сервис типов чтения")
    @Test
    public void testAddReadingType() {
        String name = "type1";

        doNothing().when(readingTypeService).addReadingType(name);

        readingTypeController.addReadingType(name);

        verify(readingTypeService, times(1)).addReadingType(name);
    }

    @DisplayName("Тестирование метода getReadingType с проверкой корректного извлечения типа чтения из сервиса")
    @Test
    public void testGetReadingType() {
        String name = "type1";
        ReadingType type = ReadingType.Create(name);

        when(readingTypeService.getReadingType(name)).thenReturn(Optional.of(type));

        Optional<ReadingType> result = readingTypeController.getReadingType(name);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(type);
    }

    @DisplayName("Тестирование метода getReadingNames с проверкой корректного извлечения имен типов чтения из сервиса")
    @Test
    public void testGetReadingNames() {
        Set<String> names = new HashSet<>(Arrays.asList("type1", "type2"));

        when(readingTypeService.getReadingNames()).thenReturn(names);

        Set<String> result = readingTypeController.getReadingNames();

        assertThat(result).isEqualTo(names);
    }
}
