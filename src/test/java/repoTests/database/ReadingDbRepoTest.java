package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.*;
import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ReadingDbRepoTest {

    private static final YamlTestConfig config = new YamlTestConfig("application.yml");

    private static final String DB_VERSION = config.getProperty("database.test.version");
    private static final String DB_USERNAME = config.getProperty("database.test.username");
    private static final String DB_PASSWORD = config.getProperty("database.test.password");
    private static final String DB_NAME = config.getProperty("database.test.name");
    private static final String LB_SCHEMA = config.getProperty("liquibase.test.schema");
    private static final String DB_SCHEMA = config.getProperty("database.test.schema");
    private static final String LB_CHANGE_LOG = config.getProperty("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static ReadingDbRepo readingDbRepo;
    private static AuthDbRepo authDbRepo;
    private static ReadingTypeDbRepo readingTypeDbRepo;
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

        readingDbRepo = new ReadingDbRepo(connectionPool);
        authDbRepo = new AuthDbRepo(connectionPool);
        readingTypeDbRepo = new ReadingTypeDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления показания")
    @Test
    public void testAdd() throws MyDatabaseException {

        ReadingRecord record = new ReadingRecord(
                null,
                1L,
                1L,
                100,
                LocalDate.now()
        );

        Optional<ReadingRecord> result = readingDbRepo.add(record);

        assertThat(result).isPresent();
        assertThat(result.get().value()).isEqualTo(record.value());
        assertThat(result.get().userId()).isEqualTo(record.userId());
        assertThat(result.get().typeId()).isEqualTo(record.typeId());
        assertThat(result.get().date()).isEqualTo(record.date());
    }

    @DisplayName("Тестирование получения показаний по номеру счета")
    @Test
    public void testGetAllByAccountNum() throws MyDatabaseException {

        UserRecord user = new UserRecord(
                2L,
                "testAccountNum",
                "password",
                1L
        );

        authDbRepo.addUser(user);

        ReadingRecord record = new ReadingRecord(
                null,
                user.id(),
                2L,
                10,
                LocalDate.now()
        );

        readingDbRepo.add(record);

        List<UserReadingRecord> result = readingDbRepo.getAllByAccountNum(user.accountNum());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).accountNum()).isEqualTo(user.accountNum());
        assertThat(result.get(0).value()).isEqualTo(record.value());
    }

    @DisplayName("Тестирование получения всех показаний")
    @Test
    public void testGetAll() throws MyDatabaseException {

        ReadingRecord record = new ReadingRecord(
                null,
                1L,
                3L,
                100,
                LocalDate.now()
        );

        readingDbRepo.add(record);

        List<UserReadingRecord> result = readingDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}
