package controllerTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.enums.Role;
import org.kharisov.in.controllers.AuthController;
import org.kharisov.services.memoryImpls.AuthMemoryService;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования контроллера аутентификации.
 */
public class AuthControllerTest {
    private final String ACCOUNT_NUM = "1000200030004000";
    private final String PASSWORD = "password123";

    private AuthMemoryService authService;
    private AuthController authController;
    User user;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов AuthService и AuthController.
     */
    @BeforeEach
    public void setUp() {
        authService = Mockito.mock(AuthMemoryService.class);
        authController = new AuthController(authService);
        user = User
                .builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
    }

    @DisplayName("Тестирование метода register с проверкой корректной регистрации пользователя в сервисе аутентификации")
    @Test
    public void testRegister() {

        when(authService.userExists(ACCOUNT_NUM)).thenReturn(false);
        when(authService.addUser(any(User.class))).thenReturn(Optional.of(user));

        Optional<User> result = authController.register(ACCOUNT_NUM, PASSWORD);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(user);
    }

    @DisplayName("Тестирование метода login с проверкой прохождения аутентификации пользователя")
    @Test
    public void testLogin() {

        when(authService.logIn(ACCOUNT_NUM, PASSWORD)).thenReturn(true);
        when(authService.getUserByAccountNum(ACCOUNT_NUM)).thenReturn(Optional.ofNullable(user));

        Optional<User> result = authController.login(ACCOUNT_NUM, PASSWORD);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(user);
    }

    @DisplayName("Тестирование метода makeUserAdmin с проверкой назначения пользователя администратором при его наличии")
    @Test
    public void testMakeUserAdmin() {

        // Подготавливаем данные для теста
        String accountNum = "1234567890123456";
        when(authService.userExists(accountNum)).thenReturn(true);
        when(authService.getUserByAccountNum(accountNum)).thenReturn(Optional.of(user));
        when(authService.changeUserRole(user, Role.ADMIN)).thenReturn(true);

        // Вызываем тестируемый метод
        boolean result = authController.makeUserAdmin(accountNum);

        // Проверяем результат
        assertThat(result).isTrue();

        // Проверяем, что методы были вызваны с правильными аргументами
        verify(authService).userExists(accountNum);
        verify(authService).getUserByAccountNum(accountNum);
        verify(authService).changeUserRole(user, Role.ADMIN);
    }

    @DisplayName("Тестирование метода makeUserAdminIfUserDoesNotExist с проверкой отсутствия назначения пользователя администратором при его отсутствии")
    @Test
    public void testMakeUserAdminIfUserDoesNotExist() {

        when(authService.userExists(ACCOUNT_NUM)).thenReturn(false);

        boolean result = authController.makeUserAdmin(ACCOUNT_NUM);

        verify(authService, times(1)).userExists(ACCOUNT_NUM);
        assertThat(result).isFalse();
    }

    @DisplayName("Тестирование метода addAdmin с проверкой корректного добавления администратора в сервис аутентификации")
    @Test
    public void testAddAdmin() {
        User admin = User
                .builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .role(Role.ADMIN)
                .build();

        when(authService.addUser(any(User.class))).thenReturn(Optional.of(admin));

        authController.addAdmin(ACCOUNT_NUM, PASSWORD);

        verify(authService, times(1)).addUser(any(User.class));
    }

    @DisplayName("Тестирование метода isAdminByAccountNum с проверкой определения, является ли пользователь администратором по номеру аккаунта")
    @Test
    public void testIsAdminByAccountNum() {

        when(authService.isAdminByAccountNum(ACCOUNT_NUM)).thenReturn(true);

        boolean result = authController.isAdminByAccountNum(ACCOUNT_NUM);

        assertThat(result).isTrue();
    }
}

