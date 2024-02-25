package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.*;
import org.kharisov.entities.ReadingTypeRecord;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.ReadingTypeDbRepo;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ReadingTypeRecordDbRepoTest {

    private static final YamlTestConfig config = new YamlTestConfig("application.yml");

    private static final String DB_VERSION = config.getProperty("database.test.version");
    private static final String DB_USERNAME = config.getProperty("database.test.username");
    private static final String DB_PASSWORD = config.getProperty("database.test.password");
    private static final String DB_NAME = config.getProperty("database.test.name");
    private static final String LB_SCHEMA = config.getProperty("liquibase.test.schema");
    private static final String DB_SCHEMA = config.getProperty("database.test.schema");
    private static final String LB_CHANGE_LOG = config.getProperty("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
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

        readingTypeDbRepo = new ReadingTypeDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления типа показаний")
    @Test
    public void testAddReadingType() throws MyDatabaseException {
        ReadingTypeRecord readingTypeRecord = new ReadingTypeRecord(
                null,
                "test1"
        );

        Optional<ReadingTypeRecord> result = readingTypeDbRepo.add(readingTypeRecord);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(readingTypeRecord.name());
    }

    @DisplayName("Тестирование получения типа показания по названию")
    @Test
    public void testGetReadingTypeByName() throws MyDatabaseException {
        String testName = "test2";

        ReadingTypeRecord readingTypeRecord = new ReadingTypeRecord(
                null,
                testName
        );
        readingTypeDbRepo.add(readingTypeRecord);

        Optional<ReadingTypeRecord> result = readingTypeDbRepo.getByName(testName);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(testName);
    }

    @DisplayName("Тестирование получения всех типов показаний")
    @Test
    public void testGetAllReadingTypes() throws MyDatabaseException {
        ReadingTypeRecord readingTypeRecord = new ReadingTypeRecord(
                null,
                "test3"
        );
        readingTypeDbRepo.add(readingTypeRecord);

        List<ReadingTypeRecord> result = readingTypeDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}
