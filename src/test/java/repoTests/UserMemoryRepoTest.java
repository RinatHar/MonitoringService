package repoTests;

import org.junit.jupiter.api.*;
import org.kharisov.entities.User;
import org.kharisov.repos.memoryImpls.UserMemoryRepo;
import org.kharisov.storages.UserMemoryStorage;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс для тестирования репозитория пользователей в памяти.
 */
public class UserMemoryRepoTest {

    private UserMemoryStorage userStorage;
    private UserMemoryRepo userMemoryRepo;

    /**
     * Метод для настройки перед каждым тестом.
     * Инициализирует экземпляры классов UserMemoryStorage и UserMemoryRepo.
     */
    @BeforeEach
    public void setUp() {
        userStorage = Mockito.mock(UserMemoryStorage.class);
        userMemoryRepo = new UserMemoryRepo(userStorage);
    }

    /**
     * Тестирование метода addUser.
     * Проверяет, что пользователь корректно добавляется в хранилище.
     * Добавляет пользователя в хранилище и затем проверяет, что он был добавлен.
     */
    @Test
    public void testAddUser() {
        User user = User.builder().build();
        Map<String, User> storage = new HashMap<>();
        storage.put(user.getAccountNum(), user);

        when(userStorage.getStorage()).thenReturn(storage);

        User result = userMemoryRepo.addUser(user);

        assertThat(result).isEqualTo(user);
        verify(userStorage, times(1)).getStorage();
    }

    /**
     * Тестирование метода getUser.
     * Проверяет, что пользователь корректно извлекается из хранилища.
     * Добавляет пользователя в хранилище и затем проверяет, что он был корректно извлечен.
     */
    @Test
    public void testGetUser() {
        User user = User.builder().build();
        Map<String, User> storage = new HashMap<>();
        storage.put(user.getAccountNum(), user);

        when(userStorage.getStorage()).thenReturn(storage);

        User result = userMemoryRepo.getUser(user.getAccountNum());

        assertThat(result).isEqualTo(user);
        verify(userStorage, times(1)).getStorage();
    }

    /**
     * Тестирование метода getAllUsers.
     * Проверяет, что все пользователи корректно извлекаются из хранилища.
     * Добавляет пользователей в хранилище и затем проверяет, что они были корректно извлечены.
     */
    @Test
    public void testGetAllUsers() {
        User user1 = User.builder().build();
        User user2 = User.builder().build();
        Map<String, User> storage = new HashMap<>();
        storage.put(user1.getAccountNum(), user1);
        storage.put(user2.getAccountNum(), user2);

        when(userStorage.getStorage()).thenReturn(storage);

        Map<String, User> result = userMemoryRepo.getAllUsers();

        assertThat(result).isEqualTo(storage);
        verify(userStorage, times(1)).getStorage();
    }
}

