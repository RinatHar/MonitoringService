package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.Config;
import org.kharisov.configs.ConnectionPoolConfig;
import org.kharisov.dtos.db.UserDto;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class UserDbRepoTest {

    private static final String DB_VERSION = Config.get("database.test.version");
    private static final String DB_USERNAME = Config.get("database.test.username");
    private static final String DB_PASSWORD = Config.get("database.test.password");
    private static final String DB_NAME = Config.get("database.test.name");
    private static final String LB_SCHEMA = Config.get("liquibase.test.schema");
    private static final String DB_SCHEMA = Config.get("database.test.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
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
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setUrl(postgres.getJdbcUrl());
        config.setUser(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        connectionPool = new ConnectionPool(config);

        userDbRepo = new UserDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления пользователя")
    @Test
    public void testAddUser() {
        UserDto userDto = new UserDto();
        userDto.setAccountNum("testAccountNum1");
        userDto.setPassword("testPassword1");
        userDto.setRoleId(1L);

        Optional<UserDto> result = userDbRepo.add(userDto);

        assertThat(result).isPresent();
        assertThat(result.get().getAccountNum()).isEqualTo(userDto.getAccountNum());
        assertThat(result.get().getPassword()).isEqualTo(userDto.getPassword());
        assertThat(result.get().getRoleId()).isEqualTo(userDto.getRoleId());
    }

    @DisplayName("Тестирование получения пользователя по номеру счета")
    @Test
    public void testGetUserByAccountNum() {
        String testAccountNum = "testAccountNum2";

        UserDto userDto = new UserDto();
        userDto.setAccountNum(testAccountNum);
        userDto.setPassword("testPassword2");
        userDto.setRoleId(1L);
        userDbRepo.add(userDto);

        Optional<UserDto> result = userDbRepo.getByAccountNum(testAccountNum);

        assertThat(result).isPresent();
        assertThat(result.get().getAccountNum()).isEqualTo(testAccountNum);
    }

    @DisplayName("Тестирование получения всех пользователей")
    @Test
    public void testGetAllUsers() {
        UserDto userDto = new UserDto();
        userDto.setAccountNum("testAccountNum3");
        userDto.setPassword("testPassword3");
        userDto.setRoleId(1L);
        userDbRepo.add(userDto);

        List<UserDto> result = userDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}
