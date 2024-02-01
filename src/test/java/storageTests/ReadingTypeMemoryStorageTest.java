package storageTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.ReadingType;
import org.kharisov.storages.ReadingTypeMemoryStorage;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования хранилища типов чтения в памяти.
 */
public class ReadingTypeMemoryStorageTest {

    private ReadingTypeMemoryStorage readingTypeMemoryStorage;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляр класса ReadingTypeMemoryStorage.
     */
    @BeforeEach
    public void setUp() {
        readingTypeMemoryStorage = ReadingTypeMemoryStorage.getInstance();
    }

    /**
     * Тестирование метода getStorage.
     * Проверяет, что данные корректно сохраняются в хранилище.
     * Добавляет данные в хранилище и затем получает их.
     * Проверяет, что полученные данные соответствуют добавленным.
     */
    @Test
    public void testGetStorage() {
        String name = "type1";
        ReadingType type = ReadingType.Create(name);

        // Добавляем данные в хранилище
        readingTypeMemoryStorage.getStorage().put(name, type);

        // Получаем данные из хранилища
        Map<String, ReadingType> storage = readingTypeMemoryStorage.getStorage();

        // Проверяем, что данные корректно сохранены в хранилище
        assertThat(storage).containsKey(name);
        assertThat(storage.get(name)).isEqualTo(type);
    }
}
