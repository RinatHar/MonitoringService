package serviceTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.services.memoryImpls.AuditMemoryService;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования сервиса аудита.
 */
public class AuditServiceTest {

    private AuditRepo auditRepo;
    private AuditMemoryService auditService;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов AuditRepo и AuditService.
     */
    @BeforeEach
    public void setUp() {
        auditRepo = Mockito.mock(AuditRepo.class);
        auditService = new AuditMemoryService(auditRepo);
    }

    /**
     * Тестирование метода logAction.
     * Проверяет, что действие корректно записывается в репозиторий аудита.
     * Добавляет действие в репозиторий и затем проверяет, что оно было добавлено.
     */
    @Test
    public void testLogAction() {
        User user = User.builder().build();
        String action = "action1";

        doNothing().when(auditRepo).logAction(user.getAccountNum(), action);

        auditService.addEntry(user, action);

        verify(auditRepo, times(1)).logAction(user.getAccountNum(), action);
    }

    /**
     * Тестирование метода getLogs.
     * Проверяет, что журналы действий пользователя корректно извлекаются из репозитория.
     * Добавляет журнал действий в репозиторий и затем проверяет, что он был корректно извлечен.
     */
    @Test
    public void testGetEntries() {
        User user = User.builder().build();
        String action = "action1";

        when(auditRepo.getLogs(user.getAccountNum())).thenReturn(List.of(action));

        List<String> logs = auditService.getEntries(user);

        assertThat(logs).containsExactly(action);
    }

    /**
     * Тестирование метода getAllLogs.
     * Проверяет, что все журналы действий корректно извлекаются из репозитория.
     * Добавляет журналы действий в репозиторий и затем проверяет, что они были корректно извлечены.
     */
    @Test
    public void testGetAllEntries() {
        User user = User.builder().build();
        String action = "action1";

        Map<String, List<String>> storage = new HashMap<>();
        storage.put(user.getAccountNum(), List.of(action));

        when(auditRepo.getAllLogs()).thenReturn(storage);

        Map<String, List<String>> allEntries = auditService.getAllEntries();

        assertThat(allEntries).isEqualTo(storage);
    }
}
