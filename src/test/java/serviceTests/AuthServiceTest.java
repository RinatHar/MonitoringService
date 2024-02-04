package serviceTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.enums.Role;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.services.memoryImpls.AuthMemoryService;
import org.kharisov.utils.AuthUtils;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования сервиса аутентификации.
 */
public class AuthServiceTest {
    private final String ACCOUNT_NUM = "1000200030004000";
    private final String PASSWORD = "password123";

    UserRepo userRepo;
    AuthMemoryService authService;
    User user;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов UserRepo и AuthService.
     */
    @BeforeEach
    public void setUp() {
        userRepo = Mockito.mock(UserRepo.class);
        authService = new AuthMemoryService(userRepo);
        user = User
                .builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
    }

    @DisplayName("Тестирование метода userExists с проверкой на существование пользователя")
    @Test
    public void testUserExists() {

        when(userRepo.getUser(ACCOUNT_NUM)).thenReturn(Optional.ofNullable(user));

        boolean exists = authService.userExists(ACCOUNT_NUM);

        assertThat(exists).isTrue();
    }

    @DisplayName("Тестирование метода getUserByAccountNum с проверкой получения пользователя по номеру аккаунта")
    @Test
    public void testGetUserByAccountNum() {

        when(userRepo.getUser(ACCOUNT_NUM)).thenReturn(Optional.ofNullable(user));

        Optional<User> result = authService.getUserByAccountNum(ACCOUNT_NUM);

        assertThat(result).isEqualTo(Optional.of(user));
    }

    @DisplayName("Тестирование метода addUserSuccess с проверкой успешного добавления пользователя")
    @Test
    public void testAddUserSuccess() {

        when(userRepo.addUser(user)).thenReturn(Optional.ofNullable(user));

        Optional<User> result = authService.addUser(user);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(user);
    }

    @DisplayName("Тестирование метода addUserFail с проверкой " +
            "на отсутствие добавления пользователя с недопустимыми данными")
    @Test
    public void testAddUserFail() {
        String accountNum = "1234";
        String password = "password123";
        User user = User
                .builder()
                .accountNum(accountNum)
                .password(password)
                .build();

        when(userRepo.addUser(user)).thenReturn(Optional.ofNullable(user));

        assert user != null;
        Optional<User> result = authService.addUser(user);

        assertThat(result).isEmpty();
    }

    @DisplayName("Тестирование метода logIn с проверкой прохождения аутентификации пользователя")
    @Test
    void testLogIn() {
        user = Mockito.mock(User.class);

        Mockito.when(authService.getUserByAccountNum(ACCOUNT_NUM)).thenReturn(Optional.ofNullable(user));
        Mockito.when(user.getPassword()).thenReturn(AuthUtils.hashPassword(PASSWORD));

        // Проверяем, что пользователь может войти в систему
        assertThat(authService.logIn(ACCOUNT_NUM, PASSWORD)).isTrue();

        // Проверяем, что пользователь не может войти в систему с неверным паролем
        assertThat(authService.logIn(ACCOUNT_NUM, "wrongPassword")).isFalse();
    }

    @DisplayName("Тестирование метода isAdminByAccountNum с проверкой, " +
            "является ли пользователь администратором по номеру аккаунта")
    @Test
    public void isAdminByAccountNum() {
        user = User
                .builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .role(Role.ADMIN)
                .build();

        when(userRepo.getUser(ACCOUNT_NUM)).thenReturn(Optional.ofNullable(user));

        boolean isAdmin = authService.isAdminByAccountNum(ACCOUNT_NUM);

        assertThat(isAdmin).isTrue();
    }

    @DisplayName("Тестирование метода testPasswordHashing с проверкой " +
            "создания правильного хеша пароля и проверки неправильного пароля")
    @Test
    public void testPasswordHashing() {
        String password = "TestPassword123";

        String hashedPassword = AuthUtils.hashPassword(password);

        // Проверяем, что хеш создан правильно
        assertThat(AuthUtils.checkPassword(password, hashedPassword)).isTrue();

        // Проверяем, что неправильный пароль не проходит проверку
        assertThat(AuthUtils.checkPassword("WrongPassword", hashedPassword)).isFalse();
    }
}