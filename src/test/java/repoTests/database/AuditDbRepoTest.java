package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.Config;
import org.kharisov.dtos.*;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class AuditDbRepoTest {

    private static final String DB_VERSION = Config.get("database.test.version");
    private static final String DB_USERNAME = Config.get("database.test.username");
    private static final String DB_PASSWORD = Config.get("database.test.password");
    private static final String DB_NAME = Config.get("database.test.name");
    private static final String LB_SCHEMA = Config.get("liquibase.test.schema");
    private static final String DB_SCHEMA = Config.get("database.test.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static AuditDbRepo auditDbRepo;
    private static UserDbRepo userDbRepo;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:" + DB_VERSION)
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD);

    @BeforeAll
    public static void setupDatabase() {
        try {
            LiquibaseExample.runMigration(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), LB_SCHEMA, DB_SCHEMA, LB_CHANGE_LOG);
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
        // Создаем пул соединений
        connectionPool = new ConnectionPool(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                2
        );

        // Создаем экземпляр AuditDbRepo с этим пулом соединений
        auditDbRepo = new AuditDbRepo(connectionPool);
        // Создаем экземпляр UserDbRepo с этим пулом соединений
        userDbRepo = new UserDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления записи")
    @Test
    public void testAdd() throws SQLException {
        EntryDto entryDto = new EntryDto();
        entryDto.setAction("action");
        entryDto.setUserId(1L);

        Optional<EntryDto> result = auditDbRepo.add(entryDto);

        assertThat(result).isPresent();
        assertThat(result.get().getAction()).isEqualTo(entryDto.getAction());
        assertThat(result.get().getUserId()).isEqualTo(entryDto.getUserId());
    }

    @DisplayName("Тестирование получения записей по номеру счета")
    @Test
    public void testGetEntriesByAccountNum() throws SQLException {

        // Создаем тестового пользователя
        UserDto userDto = new UserDto();
        userDto.setAccountNum("testAccountNum");
        userDto.setPassword("password");
        userDto.setRoleId(1L);

        // Добавляем пользователя в базу данных
        userDbRepo.add(userDto);

        // Создаем экземпляр AuditDbRepo с этим пулом соединений
        AuditDbRepo auditDbRepo = new AuditDbRepo(connectionPool);

        // Создаем тестовые данные
        EntryDto entryDto = new EntryDto();
        entryDto.setAction("action");
        entryDto.setUserId(userDto.getId());

        // Добавляем элемент в базу данных
        auditDbRepo.add(entryDto);

        // Вызываем метод getEntriesByAccountNum и проверяем результат
        List<EntryDto> result = auditDbRepo.getEntriesByAccountNum(userDto.getAccountNum());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAccountNum()).isEqualTo(userDto.getAccountNum());
    }

    @DisplayName("Тестирование получения всех записей")
    @Test
    public void testGetAll() throws SQLException {
        // Создаем тестовые данные
        EntryDto entryDto = new EntryDto();
        entryDto.setAction("action");
        entryDto.setUserId(1L);

        // Добавляем элемент в базу данных
        auditDbRepo.add(entryDto);

        // Вызываем метод getAll и проверяем результат
        List<EntryDto> result = auditDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}