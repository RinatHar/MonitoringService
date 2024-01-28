package serviceTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.services.AuthService;
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
    AuthService authService;
    User user;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов UserRepo и AuthService.
     */
    @BeforeEach
    public void setUp() {
        userRepo = Mockito.mock(UserRepo.class);
        authService = new AuthService(userRepo);
        user = User
                .builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .build();
    }

    /**
     * Тестирование метода userExists.
     * Проверяет, существует ли пользователь.
     */
    @Test
    public void testUserExists() {

        when(userRepo.getUser(ACCOUNT_NUM)).thenReturn(user);

        boolean exists = authService.userExists(ACCOUNT_NUM);

        assertThat(exists).isTrue();
    }

    /**
     * Тестирование метода getUserByAccountNum.
     * Проверяет, получает ли сервис пользователя по номеру аккаунта.
     */
    @Test
    public void testGetUserByAccountNum() {

        when(userRepo.getUser(ACCOUNT_NUM)).thenReturn(user);

        User result = authService.getUserByAccountNum(ACCOUNT_NUM);

        assertThat(result).isEqualTo(user);
    }

    /**
     * Тестирование метода addUserSuccess.
     * Проверяет, успешно ли добавляется пользователь.
     */
    @Test
    public void testAddUserSuccess() {

        when(userRepo.addUser(user)).thenReturn(user);

        Optional<User> result = authService.addUser(user);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(user);
    }

    /**
     * Тестирование метода addUserFail.
     * Проверяет, что при добавлении пользователя с недопустимыми данными возвращается пустой результат.
     */
    @Test
    public void testAddUserFail() {
        String accountNum = "1234";
        String password = "password123";
        User user = User
                .builder()
                .accountNum(accountNum)
                .password(password)
                .build();

        when(userRepo.addUser(user)).thenReturn(user);

        Optional<User> result = authService.addUser(user);

        assertThat(result).isEmpty();
    }

    /**
     * Тестирование метода logIn.
     * Проверяет, проходит ли аутентификация пользователя.
     */
    @Test
    void testLogIn() {
        user = Mockito.mock(User.class);

        Mockito.when(authService.getUserByAccountNum(ACCOUNT_NUM)).thenReturn(user);
        Mockito.when(user.getPassword()).thenReturn(authService.hashPassword(PASSWORD));

        // Проверяем, что пользователь может войти в систему
        assertThat(authService.logIn(ACCOUNT_NUM, PASSWORD)).isTrue();

        // Проверяем, что пользователь не может войти в систему с неверным паролем
        assertThat(authService.logIn(ACCOUNT_NUM, "wrongPassword")).isFalse();
    }

    /**
     * Тестирование метода isAdminByAccountNum.
     * Проверяет, является ли пользователь администратором по номеру аккаунта.
     */
    @Test
    public void isAdminByAccountNum() {
        user = User
                .builder()
                .accountNum(ACCOUNT_NUM)
                .password(PASSWORD)
                .isAdmin(true)
                .build();

        when(userRepo.getUser(ACCOUNT_NUM)).thenReturn(user);

        boolean isAdmin = authService.isAdminByAccountNum(ACCOUNT_NUM);

        assertThat(isAdmin).isTrue();
    }

    /**
     * Тестирование метода testPasswordHashing.
     * Проверяет, что хеш пароля создается правильно и что неправильный пароль не проходит проверку.
     */
    @Test
    public void testPasswordHashing() {
        String password = "TestPassword123";

        String hashedPassword = authService.hashPassword(password);

        // Проверяем, что хеш создан правильно
        assertThat(authService.checkPassword(password, hashedPassword)).isTrue();

        // Проверяем, что неправильный пароль не проходит проверку
        assertThat(authService.checkPassword("WrongPassword", hashedPassword)).isFalse();
    }
}