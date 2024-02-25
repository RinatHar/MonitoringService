package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.*;
import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.AuthDbRepo;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class AuthDbRepoTest {

    private static final YamlTestConfig config = new YamlTestConfig("application.yml");

    private static final String DB_VERSION = config.getProperty("database.test.version");
    private static final String DB_USERNAME = config.getProperty("database.test.username");
    private static final String DB_PASSWORD = config.getProperty("database.test.password");
    private static final String DB_NAME = config.getProperty("database.test.name");
    private static final String LB_SCHEMA = config.getProperty("liquibase.test.schema");
    private static final String DB_SCHEMA = config.getProperty("database.test.schema");
    private static final String LB_CHANGE_LOG = config.getProperty("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static AuthDbRepo authDbRepo;
    private static LiquibaseExample liquibaseExample;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:" + DB_VERSION)
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD);

    @BeforeAll
    public static void setupDatabase() {
        liquibaseExample = new LiquibaseExample(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                LB_SCHEMA,
                DB_SCHEMA,
                LB_CHANGE_LOG);
        try {
            liquibaseExample.runMigration();
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }

        connectionPool = new ConnectionPool(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                1);

        authDbRepo = new AuthDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления пользователя")
    @Test
    public void testAddUser() throws MyDatabaseException {
        UserRecord user = new UserRecord(
                null,
                "testAccountNum1",
                "testPassword1",
                1L
        );

        Optional<UserRecord> result = authDbRepo.addUser(user);

        assertThat(result).isPresent();
        assertThat(result.get().accountNum()).isEqualTo(user.accountNum());
        assertThat(result.get().password()).isEqualTo(user.password());
        assertThat(result.get().role_id()).isEqualTo(user.role_id());
    }

    @DisplayName("Тестирование получения пользователя по номеру счета")
    @Test
    public void testGetUserByAccountNum() throws MyDatabaseException {
        String testAccountNum = "testAccountNum2";

        UserRecord user = new UserRecord(
                null,
                testAccountNum,
                "testPassword2",
                1L
        );
        authDbRepo.addUser(user);

        Optional<UserRecord> result = authDbRepo.getUserByAccountNum(testAccountNum);

        assertThat(result).isPresent();
        assertThat(result.get().accountNum()).isEqualTo(testAccountNum);
    }

    @DisplayName("Тестирование получения всех пользователей")
    @Test
    public void testGetAllUsers() throws MyDatabaseException {
        UserRecord user = new UserRecord(
                null,
                "testAccountNum3",
                "testPassword2",
                1L
        );
        authDbRepo.addUser(user);

        List<UserRecord> result = authDbRepo.getAllUsers();

        assertThat(result).isNotEmpty();
    }

    @DisplayName("Тестирование добавления роли")
    @Test
    public void testAddRole() throws MyDatabaseException {
        RoleRecord record = new RoleRecord(
                null,
                "test1"
        );

        Optional<RoleRecord> result = authDbRepo.addRole(record);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(record.name());
    }

    @DisplayName("Тестирование получения роли по ID")
    @Test
    public void testGetRoleById() throws MyDatabaseException {
        Long testId = 1L;

        RoleRecord record = new RoleRecord(
                null,
                "test2"
        );
        authDbRepo.addRole(record);

        Optional<RoleRecord> result = authDbRepo.getRoleById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(testId);
    }

    @DisplayName("Тестирование получения роли по имени")
    @Test
    public void testGetRoleByName() throws MyDatabaseException {
        Role testRole = Role.USER;
        Optional<RoleRecord> result = authDbRepo.getRoleByName(testRole);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(testRole.name());
    }

    @DisplayName("Тестирование получения всех ролей")
    @Test
    public void testGetAllRoles() throws MyDatabaseException {
        RoleRecord record = new RoleRecord(
                null,
                "test4"
        );
        authDbRepo.addRole(record);

        List<RoleRecord> result = authDbRepo.getAllRoles();

        assertThat(result).isNotEmpty();
    }
}
