package org.kharisov.liquibase;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.*;
import org.kharisov.configs.Config;

import java.sql.*;

public class LiquibaseExample {
    private static final String DB_URL = Config.get("database.url");
    private static final String DB_USERNAME = Config.get("database.username");
    private static final String DB_PASSWORD = Config.get("database.password");
    private static final String DB_SCHEMA = Config.get("database.schema");
    private static final String LB_SCHEMA = Config.get("liquibase.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.change_log");

    public static void main(String[] args) {
        try {
            LiquibaseExample.runMigration();
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }
    public static void runMigration() throws SQLException, DatabaseException, CommandExecutionException {
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

        System.out.println("Migration is complete successfully");
    }
}
