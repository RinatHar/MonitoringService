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
 * Класс для тестирования репозитория типов чтения в памяти.
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

    /**
     * Тестирование метода addReadingType.
     * Проверяет, что тип чтения корректно добавляется в хранилище.
     * Добавляет тип чтения в хранилище и затем проверяет, что он был добавлен.
     */
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

    /**
     * Тестирование метода getReadingType.
     * Проверяет, что тип чтения корректно извлекается из хранилища.
     * Добавляет тип чтения в хранилище и затем проверяет, что он был корректно извлечен.
     */
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

    /**
     * Тестирование метода getReadingNames.
     * Проверяет, что имена типов чтения корректно извлекаются из хранилища.
     * Добавляет типы чтения в хранилище и затем проверяет, что их имена были корректно извлечены.
     */
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

