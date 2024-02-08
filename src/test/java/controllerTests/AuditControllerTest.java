package controllerTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.in.controllers.AuditController;
import org.kharisov.services.interfaces.AuditService;
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

    @DisplayName("Тестирование метода addEntry с проверкой корректной записи действия в сервис аудита")
    @Test
    public void testAddEntry() {
        User user = User.builder().build();
        String action = "action1";

        doNothing().when(auditService).addEntry(user, action);

        auditController.addEntry(user, action);

        verify(auditService, times(1)).addEntry(user, action);
    }

    @DisplayName("Тестирование метода getEntries с проверкой корректного извлечения журналов действий пользователя из сервиса")
    @Test
    public void testGetEntries() {
        User user = User.builder().build();
        List<String> entries = Arrays.asList("action1", "action2");

        when(auditService.getEntries(user)).thenReturn(entries);

        List<String> result = auditController.getEntries(user);

        assertThat(result).isEqualTo(entries);
    }

    @DisplayName("Тестирование метода getAllEntries с проверкой корректного извлечения всех журналов действий из сервиса")
    @Test
    public void testGetAllEntries() {
        User user1 = User.builder().build();
        User user2 = User.builder().build();
        List<String> entries1 = Arrays.asList("action1", "action2");
        List<String> entries2 = Arrays.asList("action3", "action4");
        Map<String, List<String>> allEntries = new HashMap<>();
        allEntries.put(user1.getAccountNum(), entries1);
        allEntries.put(user2.getAccountNum(), entries2);

        when(auditService.getAllEntries()).thenReturn(allEntries);

        Map<String, List<String>> result = auditController.getAllEntries();

        assertThat(result).isEqualTo(allEntries);
    }
}

