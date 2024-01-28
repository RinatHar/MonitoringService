package controllerTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.in.controllers.AuditController;
import org.kharisov.services.AuditService;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования контроллера аудита.
 */
public class AuditControllerTest {

    private AuditService auditService;
    private AuditController auditController;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов AuditService и AuditController.
     */
    @BeforeEach
    public void setUp() {
        auditService = Mockito.mock(AuditService.class);
        auditController = new AuditController(auditService);
    }

    /**
     * Тестирование метода logAction.
     * Проверяет, что действие корректно записывается в сервис аудита.
     * Добавляет действие в сервис и затем проверяет, что оно было добавлено.
     */
    @Test
    public void testLogAction() {
        User user = User.builder().build();
        String action = "action1";

        doNothing().when(auditService).logAction(user, action);

        auditController.logAction(user, action);

        verify(auditService, times(1)).logAction(user, action);
    }

    /**
     * Тестирование метода getLogs.
     * Проверяет, что журналы действий пользователя корректно извлекаются из сервиса.
     * Добавляет журнал действий в сервис и затем проверяет, что он был корректно извлечен.
     */
    @Test
    public void testGetLogs() {
        User user = User.builder().build();
        List<String> logs = Arrays.asList("action1", "action2");

        when(auditService.getLogs(user)).thenReturn(logs);

        List<String> result = auditController.getLogs(user);

        assertThat(result).isEqualTo(logs);
    }

    /**
     * Тестирование метода getAllLogs.
     * Проверяет, что все журналы действий корректно извлекаются из сервиса.
     * Добавляет журналы действий в сервис и затем проверяет, что они были корректно извлечены.
     */
    @Test
    public void testGetAllLogs() {
        User user1 = User.builder().build();
        User user2 = User.builder().build();
        List<String> logs1 = Arrays.asList("action1", "action2");
        List<String> logs2 = Arrays.asList("action3", "action4");
        Map<String, List<String>> allLogs = new HashMap<>();
        allLogs.put(user1.getAccountNum(), logs1);
        allLogs.put(user2.getAccountNum(), logs2);

        when(auditService.getAllLogs()).thenReturn(allLogs);

        Map<String, List<String>> result = auditController.getAllLogs();

        assertThat(result).isEqualTo(allLogs);
    }
}

