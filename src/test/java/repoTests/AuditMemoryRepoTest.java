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

    @DisplayName("Тестирование метода logAction с проверкой корректной записи " +
            "действия в хранилище аудита")
    @Test
    public void testLogAction() {
        storage.put(ACCOUNT_NUM, new ArrayList<>(List.of(ACTION)));

        when(auditStorage.getStorage()).thenReturn(storage);

        auditMemoryRepo.logAction(ACCOUNT_NUM, ACTION);

        verify(auditStorage, times(2)).getStorage();
    }

    @DisplayName("Тестирование метода getLogs с проверкой корректного извлечения " +
            "журналов действий пользователя из хранилища")
    @Test
    public void testGetLogs() {
        storage.put(ACCOUNT_NUM, List.of(ACTION));

        when(auditStorage.getStorage()).thenReturn(storage);

        List<String> logs = auditMemoryRepo.getLogs(ACCOUNT_NUM);

        assertThat(logs).containsExactly(ACTION);
    }

    @DisplayName("Тестирование метода getAllLogs с проверкой корректного извлечения " +
            "всех журналов действий из хранилища")
    @Test
    public void testGetAllLogs() {
        storage.put(ACCOUNT_NUM, List.of(ACTION));

        when(auditStorage.getStorage()).thenReturn(storage);

        Map<String, List<String>> allLogs = auditMemoryRepo.getAllLogs();

        assertThat(allLogs).isEqualTo(storage);
    }
}

