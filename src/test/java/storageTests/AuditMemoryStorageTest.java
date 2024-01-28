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
        auditMemoryStorage = new AuditMemoryStorage();
    }

    /**
     * Тестирование метода getStorage.
     * Проверяет, что данные корректно сохраняются в хранилище.
     * Добавляет данные в хранилище и затем получает их.
     * Проверяет, что полученные данные соответствуют добавленным.
     */
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
