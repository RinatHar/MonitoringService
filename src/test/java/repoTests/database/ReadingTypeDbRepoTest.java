package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.Config;
import org.kharisov.configs.ConnectionPoolConfig;
import org.kharisov.dtos.ReadingTypeDto;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ReadingTypeDbRepoTest {

    private static final String DB_VERSION = Config.get("database.test.version");
    private static final String DB_USERNAME = Config.get("database.test.username");
    private static final String DB_PASSWORD = Config.get("database.test.password");
    private static final String DB_NAME = Config.get("database.test.name");
    private static final String LB_SCHEMA = Config.get("liquibase.test.schema");
    private static final String DB_SCHEMA = Config.get("database.test.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static ReadingTypeDbRepo readingTypeDbRepo;

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

        readingTypeDbRepo = new ReadingTypeDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления типа показаний")
    @Test
    public void testAddReadingType() {
        ReadingTypeDto readingTypeDto = new ReadingTypeDto();
        readingTypeDto.setName("test1");

        Optional<ReadingTypeDto> result = readingTypeDbRepo.add(readingTypeDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(readingTypeDto.getName());
    }

    @DisplayName("Тестирование получения типа показания по названию")
    @Test
    public void testGetReadingTypeByName() {
        String testName = "test2";

        ReadingTypeDto readingTypeDto = new ReadingTypeDto();
        readingTypeDto.setName(testName);
        readingTypeDbRepo.add(readingTypeDto);

        Optional<ReadingTypeDto> result = readingTypeDbRepo.getByName(testName);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(testName);
    }

    @DisplayName("Тестирование получения всех типов показаний")
    @Test
    public void testGetAllReadingTypes() {
        ReadingTypeDto readingTypeDto = new ReadingTypeDto();
        readingTypeDto.setName("test3");
        readingTypeDbRepo.add(readingTypeDto);

        List<ReadingTypeDto> result = readingTypeDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}
