package repoTests.database;

import liquibase.exception.*;
import org.junit.jupiter.api.*;
import org.kharisov.configs.Config;
import org.kharisov.dtos.RoleDto;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class RoleDbRepoTest {

    private static final String DB_VERSION = Config.get("database.test.version");
    private static final String DB_USERNAME = Config.get("database.test.username");
    private static final String DB_PASSWORD = Config.get("database.test.password");
    private static final String DB_NAME = Config.get("database.test.name");
    private static final String LB_SCHEMA = Config.get("liquibase.test.schema");
    private static final String DB_SCHEMA = Config.get("database.test.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.test.change_log");

    private static ConnectionPool connectionPool;
    private static RoleDbRepo roleDbRepo;

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
        connectionPool = new ConnectionPool(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                2
        );

        roleDbRepo = new RoleDbRepo(connectionPool);
    }

    @DisplayName("Тестирование добавления роли")
    @Test
    public void testAddRole() {
        RoleDto roleDto = new RoleDto();
        roleDto.setName("test1");

        Optional<RoleDto> result = roleDbRepo.add(roleDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(roleDto.getName());
    }

    @DisplayName("Тестирование получения роли по ID")
    @Test
    public void testGetRoleById() {
        Long testId = 1L;

        RoleDto roleDto = new RoleDto();
        roleDto.setName("test2");
        roleDbRepo.add(roleDto);

        Optional<RoleDto> result = roleDbRepo.getById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testId);
    }

    @DisplayName("Тестирование получения роли по имени")
    @Test
    public void testGetRoleByName() {
        String testName = "test3";

        RoleDto roleDto = new RoleDto();
        roleDto.setName(testName);
        roleDbRepo.add(roleDto);

        Optional<RoleDto> result = roleDbRepo.getByName(testName);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(testName);
    }

    @DisplayName("Тестирование получения всех ролей")
    @Test
    public void testGetAllRoles() {
        RoleDto roleDto = new RoleDto();
        roleDto.setName("test4");
        roleDbRepo.add(roleDto);

        List<RoleDto> result = roleDbRepo.getAll();

        assertThat(result).isNotEmpty();
    }
}
