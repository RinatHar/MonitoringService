package storageTests;

import org.junit.jupiter.api.*;
import org.kharisov.storages.AuditMemoryStorage;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования хранилища аудита в памяти.
 */
public class AuditMemoryStorageTest {

    private AuditMemoryStorage auditMemoryStorage;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляр класса AuditMemoryStorage.
     */
    @BeforeEach
    public void setUp() {
        auditMemoryStorage = AuditMemoryStorage.getInstance();
    }

    @DisplayName("Тестирование метода getStorage с проверкой корректного сохранения данных в хранилище")
    @Test
    public void testGetStorage() {
        String accountNum = "12345";
        List<String> actions = Arrays.asList("action1", "action2");

        // Добавляем данные в хранилище
        auditMemoryStorage.getStorage().put(accountNum, actions);

        // Получаем данные из хранилища
        Map<String, List<String>> storage = auditMemoryStorage.getStorage();

        // Проверяем, что данные корректно сохранены в хранилище
        assertThat(storage).containsKey(accountNum);
        assertThat(storage.get(accountNum)).isEqualTo(actions);
    }
}
