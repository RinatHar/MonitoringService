package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.configs.*;
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
    private static final String LB_CHANGE_LOG = config.getProperty("liquibase.test.changeLog");

    private static AuthDbRepo authDbRepo;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:" + DB_VERSION)
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD);

    @BeforeAll
    public static void setupDatabase() {
        DatabaseProperties databaseProperties = new DatabaseProperties();
        LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
        databaseProperties.setUrl(postgres.getJdbcUrl());
        databaseProperties.setUsername(postgres.getUsername());
        databaseProperties.setPassword(postgres.getPassword());
        databaseProperties.setSchema(DB_SCHEMA);
        databaseProperties.setSizePool(1);
        liquibaseProperties.setSchema(LB_SCHEMA);
        liquibaseProperties.setChangeLog(LB_CHANGE_LOG);


        LiquibaseExample liquibaseExample = new LiquibaseExample(databaseProperties, liquibaseProperties);
        try {
            liquibaseExample.runMigration();
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }

        databaseProperties.setUrl(postgres.getJdbcUrl());

        ConnectionPool connectionPool = new ConnectionPool(databaseProperties);

        authDbRepo = new AuthDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления пользователя")
    @Test
    public void testAddUser() throws MyDatabaseException {
        UserRecord user = new UserRecord(
                null,
                "testAccountNum1",
                "testPassword1",
                "USER"
        );

        Optional<UserRecord> result = authDbRepo.addUser(user);

        assertThat(result).isPresent();
        assertThat(result.get().accountNum()).isEqualTo(user.accountNum());
        assertThat(result.get().password()).isEqualTo(user.password());
        assertThat(result.get().role()).isEqualTo(user.role());
    }

    @DisplayName("Тестирование получения пользователя по номеру счета")
    @Test
    public void testGetUserByAccountNum() throws MyDatabaseException {
        String testAccountNum = "testAccountNum2";

        UserRecord user = new UserRecord(
                null,
                testAccountNum,
                "testPassword2",
                "USER"
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
                "USER"
        );
        authDbRepo.addUser(user);

        List<UserRecord> result = authDbRepo.getAllUsers();

        assertThat(result).isNotEmpty();
    }
}
