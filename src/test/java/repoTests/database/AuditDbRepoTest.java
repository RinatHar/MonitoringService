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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class AuditDbRepoTest {

    private static final YamlTestConfig config = new YamlTestConfig("application.yml");

    private static final String DB_VERSION = config.getProperty("database.test.version");
    private static final String DB_USERNAME = config.getProperty("database.test.username");
    private static final String DB_PASSWORD = config.getProperty("database.test.password");
    private static final String DB_NAME = config.getProperty("database.test.name");
    private static final String LB_SCHEMA = config.getProperty("liquibase.test.schema");
    private static final String DB_SCHEMA = config.getProperty("database.test.schema");
    private static final String LB_CHANGE_LOG = config.getProperty("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static AuditDbRepo auditDbRepo;
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

        auditDbRepo = new AuditDbRepo(connectionPool);
        authDbRepo = new AuthDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления записи")
    @Test
    public void testAdd() throws MyDatabaseException {
        AuditRecord record = new AuditRecord(
                null,
                "action",
                1L
        );

        Optional<AuditRecord> result = auditDbRepo.add(record);

        assertThat(result).isPresent();
        assertThat(result.get().action()).isEqualTo(record.action());
        assertThat(result.get().userId()).isEqualTo(record.userId());
    }

    @DisplayName("Тестирование получения записей по номеру счета")
    @Test
    public void testGetEntriesByAccountNum() throws MyDatabaseException {

        UserRecord user = new UserRecord(
                2L,
                "testAccountNum",
                "password",
                1L
        );

        authDbRepo.addUser(user);

        AuditDbRepo auditDbRepo = new AuditDbRepo(connectionPool);

        AuditRecord record = new AuditRecord(
                null,
                "action",
                user.id()
        );

        auditDbRepo.add(record);

        List<UserAuditRecord> result = auditDbRepo.getAuditRecordsByAccountNum(user.accountNum());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).accountNum()).isEqualTo(user.accountNum());
    }

    @DisplayName("Тестирование получения всех записей")
    @Test
    public void testGetAll() throws MyDatabaseException {

        AuditRecord record = new AuditRecord(
                null,
                "action",
                1L
        );

        auditDbRepo.add(record);

        List<UserAuditRecord> result = auditDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}