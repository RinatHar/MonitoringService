package repoTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.ReadingType;
import org.kharisov.repos.memoryImpls.ReadingTypeMemoryRepo;
import org.kharisov.storages.ReadingTypeMemoryStorage;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования репозитория типов показаний в памяти.
 */
public class ReadingTypeMemoryRepoTest {

    private ReadingTypeMemoryStorage storage;
    private ReadingTypeMemoryRepo repo;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов ReadingTypeMemoryStorage и ReadingTypeMemoryRepo.
     */
    @BeforeEach
    public void setUp() {
        storage = Mockito.mock(ReadingTypeMemoryStorage.class);
        repo = new ReadingTypeMemoryRepo(storage);
    }

    @DisplayName("Тестирование метода addReadingType с проверкой " +
            "корректного добавления типа показаний в хранилище")
    @Test
    public void testAddReadingType() {
        String name = "type1";
        ReadingType type = ReadingType.Create(name);

        HashMap<String, ReadingType> map = new HashMap<>();
        map.put(name, type);

        when(storage.getStorage()).thenReturn(map);

        repo.addReadingType(name);

        verify(storage, times(1)).getStorage();
    }

    @DisplayName("Тестирование метода getReadingType с проверкой " +
            "корректного извлечения типа показаний из хранилища")
    @Test
    public void testGetReadingType() {
        String name = "type1";
        ReadingType type = ReadingType.Create(name);

        HashMap<String, ReadingType> map = new HashMap<>();
        map.put(name, type);

        when(storage.getStorage()).thenReturn(map);

        Optional<ReadingType> result = repo.getReadingType(name);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(type);
    }

    @DisplayName("Тестирование метода getReadingNames с проверкой " +
            "корректного извлечения имен типов показаний из хранилища")
    @Test
    public void testGetReadingNames() {
        String name1 = "type1";
        String name2 = "type2";
        ReadingType type1 = ReadingType.Create(name1);
        ReadingType type2 = ReadingType.Create(name2);

        HashMap<String, ReadingType> map = new HashMap<>();
        map.put(name1, type1);
        map.put(name2, type2);

        when(storage.getStorage()).thenReturn(map);

        Set<String> names = repo.getReadingNames();

        assertThat(names).containsExactlyInAnyOrder(name1, name2);
    }
}

