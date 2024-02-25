package org.kharisov.liquibase;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.*;
import org.kharisov.configs.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Класс LiquibaseExample предназначен для выполнения миграции базы данных с использованием Liquibase.
 * Он загружает конфигурационные параметры из файла 'application.properties' и использует их для установки соединения с базой данных и выполнения миграции.
 */
@Component
public class LiquibaseExample {
    private static final Logger logger = LoggerFactory.getLogger(LiquibaseExample.class);

    private final String DB_URL;
    private final String DB_USERNAME;
    private final String DB_PASSWORD;
    private final String DB_SCHEMA;
    private final String LB_SCHEMA;
    private final String LB_CHANGE_LOG;

    @Autowired
    public LiquibaseExample(DatabaseProperties databaseProperties,
                            LiquibaseProperties liquibaseProperties) {
        this.DB_URL = databaseProperties.getUrl();
        this.DB_USERNAME = databaseProperties.getUsername();
        this.DB_PASSWORD = databaseProperties.getPassword();
        this.DB_SCHEMA = databaseProperties.getSchema();
        this.LB_SCHEMA = liquibaseProperties.getSchema();
        this.LB_CHANGE_LOG = liquibaseProperties.getChangeLog();
    }

    /**
     * Выполняет миграцию базы данных.
     *
     * @throws SQLException              если возникает ошибка при работе с базой данных.
     * @throws DatabaseException         если возникает ошибка при работе с Liquibase.
     * @throws CommandExecutionException если возникает ошибка при выполнении команды Liquibase.
     */
    public void runMigration() throws SQLException, DatabaseException, CommandExecutionException {
        Connection connection = DriverManager.getConnection(DB_URL,  DB_USERNAME, DB_PASSWORD);

        Statement stmt = connection.createStatement();
        stmt.execute("CREATE SCHEMA IF NOT EXISTS " + LB_SCHEMA);
        stmt.execute("CREATE SCHEMA IF NOT EXISTS " + DB_SCHEMA);

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(DB_SCHEMA);
        database.setLiquibaseSchemaName(LB_SCHEMA);

        new CommandScope(UpdateCommandStep.COMMAND_NAME)
                .addArgumentValue("changeLogFile", LB_CHANGE_LOG)
                .addArgumentValue("username", DB_USERNAME)
                .addArgumentValue("password", DB_PASSWORD)
                .addArgumentValue("url", DB_URL)
                .addArgumentValue("database", database)
                .execute();

        logger.info("Migration is complete successfully");
    }
}
