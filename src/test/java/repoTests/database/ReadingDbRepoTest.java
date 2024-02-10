package repoTests.database;

import liquibase.exception.CommandExecutionException;
import liquibase.exception.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kharisov.configs.Config;
import org.kharisov.configs.ConnectionPoolConfig;
import org.kharisov.dtos.db.ReadingDto;
import org.kharisov.dtos.db.UserDto;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.ConnectionPool;
import org.kharisov.repos.databaseImpls.ReadingDbRepo;
import org.kharisov.repos.databaseImpls.UserDbRepo;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ReadingDbRepoTest {

    private static final String DB_VERSION = Config.get("database.test.version");
    private static final String DB_USERNAME = Config.get("database.test.username");
    private static final String DB_PASSWORD = Config.get("database.test.password");
    private static final String DB_NAME = Config.get("database.test.name");
    private static final String LB_SCHEMA = Config.get("liquibase.test.schema");
    private static final String DB_SCHEMA = Config.get("database.test.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static ReadingDbRepo readingDbRepo;
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

        readingDbRepo = new ReadingDbRepo(connectionPool);
        userDbRepo = new UserDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления показания")
    @Test
    public void testAdd() {

        ReadingDto readingDto = new ReadingDto();
        readingDto.setTypeId(1L);
        readingDto.setUserId(1L);
        readingDto.setValue(100);
        readingDto.setDate(LocalDate.now());

        Optional<ReadingDto> result = readingDbRepo.add(readingDto);

        assertThat(result).isPresent();
        assertThat(result.get().getValue()).isEqualTo(readingDto.getValue());
        assertThat(result.get().getUserId()).isEqualTo(readingDto.getUserId());
        assertThat(result.get().getTypeId()).isEqualTo(readingDto.getTypeId());
        assertThat(result.get().getDate()).isEqualTo(readingDto.getDate());
    }

    @DisplayName("Тестирование получения показаний по номеру счета")
    @Test
    public void testGetAllByAccountNum() {

        UserDto userDto = new UserDto();
        userDto.setAccountNum("testAccountNum");
        userDto.setPassword("password");
        userDto.setRoleId(1L);

        userDbRepo.add(userDto);

        ReadingDto readingDto = new ReadingDto();
        readingDto.setTypeId(1L);
        readingDto.setUserId(userDto.getId());
        readingDto.setValue(100);
        readingDto.setDate(LocalDate.now());

        readingDbRepo.add(readingDto);

        List<ReadingDto> result = readingDbRepo.getAllByAccountNum(userDto.getAccountNum());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAccountNum()).isEqualTo(userDto.getAccountNum());
        assertThat(result.get(0).getValue()).isEqualTo(readingDto.getValue());
    }

    @DisplayName("Тестирование получения всех показаний")
    @Test
    public void testGetAll() {

        ReadingDto readingDto = new ReadingDto();
        readingDto.setTypeId(1L);
        readingDto.setUserId(1L);
        readingDto.setValue(100);
        readingDto.setDate(LocalDate.now());

        readingDbRepo.add(readingDto);

        List<ReadingDto> result = readingDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}
