package repoTests;

import org.junit.jupiter.api.*;
import org.kharisov.repos.memoryImpls.AuditMemoryRepo;
import org.kharisov.storages.AuditMemoryStorage;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования репозитория аудита в памяти.
 */
public class AuditMemoryRepoTest {
    private final String ACCOUNT_NUM = "12345";
    private final String ACTION = "action1";

    private AuditMemoryStorage auditStorage;
    private AuditMemoryRepo auditMemoryRepo;
    Map<String, List<String>> storage;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов AuditMemoryStorage и AuditMemoryRepo.
     */
    @BeforeEach
    public void setUp() {
        auditStorage = Mockito.mock(AuditMemoryStorage.class);
        auditMemoryRepo = new AuditMemoryRepo(auditStorage);
        storage = new HashMap<>();
    }

    /**
     * Тестирование метода logAction.
     * Проверяет, что действие корректно записывается в хранилище аудита.
     * Добавляет действие в хранилище и затем проверяет, что оно было добавлено.
     */
    @Test
    public void testLogAction() {
        storage.put(ACCOUNT_NUM, new ArrayList<>(List.of(ACTION)));

        when(auditStorage.getStorage()).thenReturn(storage);

        auditMemoryRepo.logAction(ACCOUNT_NUM, ACTION);

        verify(auditStorage, times(2)).getStorage();
    }

    /**
     * Тестирование метода getLogs.
     * Проверяет, что журналы действий пользователя корректно извлекаются из хранилища.
     * Добавляет журнал действий в хранилище и затем проверяет, что он был корректно извлечен.
     */
    @Test
    public void testGetLogs() {
        storage.put(ACCOUNT_NUM, List.of(ACTION));

        when(auditStorage.getStorage()).thenReturn(storage);

        List<String> logs = auditMemoryRepo.getLogs(ACCOUNT_NUM);

        assertThat(logs).containsExactly(ACTION);
    }

    /**
     * Тестирование метода getAllLogs.
     * Проверяет, что все журналы действий корректно извлекаются из хранилища.
     * Добавляет журналы действий в хранилище и затем проверяет, что они были корректно извлечены.
     */
    @Test
    public void testGetAllLogs() {
        storage.put(ACCOUNT_NUM, List.of(ACTION));

        when(auditStorage.getStorage()).thenReturn(storage);

        Map<String, List<String>> allLogs = auditMemoryRepo.getAllLogs();

        assertThat(allLogs).isEqualTo(storage);
    }
}

