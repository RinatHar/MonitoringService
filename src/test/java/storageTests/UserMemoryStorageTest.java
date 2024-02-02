package storageTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.storages.UserMemoryStorage;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования хранилища пользователей в памяти.
 */
public class UserMemoryStorageTest {

    private UserMemoryStorage userMemoryStorage;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляр класса UserMemoryStorage.
     */
    @BeforeEach
    public void setUp() {
        userMemoryStorage = UserMemoryStorage.getInstance();
    }

    @DisplayName("Тестирование метода getStorage с проверкой корректного сохранения данных в хранилище")
    @Test
    public void testGetStorage() {
        User user = User.builder().build();

        // Добавляем данные в хранилище
        userMemoryStorage.getStorage().put(user.getAccountNum(), user);

        // Получаем данные из хранилища
        Map<String, User> result = userMemoryStorage.getStorage();

        // Проверяем, что данные корректно сохранены в хранилище
        assertThat(result).containsKey(user.getAccountNum());
        assertThat(result.get(user.getAccountNum())).isEqualTo(user);
    }
}

