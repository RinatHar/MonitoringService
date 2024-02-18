package org.kharisov.liquibase;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.*;
import org.kharisov.configs.Config;

import java.sql.*;

/**
 * Класс LiquibaseExample предназначен для выполнения миграции базы данных с использованием Liquibase.
 * Он загружает конфигурационные параметры из файла 'application.properties' и использует их для установки соединения с базой данных и выполнения миграции.
 */
public class LiquibaseExample {
    private static final String DB_URL = Config.get("database.url");
    private static final String DB_USERNAME = Config.get("database.username");
    private static final String DB_PASSWORD = Config.get("database.password");
    private static final String DB_SCHEMA = Config.get("database.schema");
    private static final String LB_SCHEMA = Config.get("liquibase.schema");
    private static final String LB_CHANGE_LOG = Config.get("liquibase.change_log");

    /**
     * Главный метод, который запускает процесс миграции.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        try {
            LiquibaseExample.runMigration(DB_URL, DB_USERNAME, DB_PASSWORD, LB_SCHEMA, DB_SCHEMA, LB_CHANGE_LOG);
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }

    /**
     * Выполняет миграцию базы данных.
     *
     * @param dbUrl       URL базы данных.
     * @param dbUsername  имя пользователя базы данных.
     * @param dbPassword  пароль пользователя базы данных.
     * @param lbSchema    схема Liquibase.
     * @param dbSchema    схема базы данных.
     * @param lbChangeLog файл журнала изменений Liquibase.
     * @throws SQLException              если возникает ошибка при работе с базой данных.
     * @throws DatabaseException         если возникает ошибка при работе с Liquibase.
     * @throws CommandExecutionException если возникает ошибка при выполнении команды Liquibase.
     */
    public static void runMigration(String dbUrl,
                                    String dbUsername,
                                    String dbPassword,
                                    String lbSchema,
                                    String dbSchema,
                                    String lbChangeLog) throws SQLException, DatabaseException, CommandExecutionException {
        Connection connection = DriverManager.getConnection(dbUrl,  dbUsername, dbPassword);

        Statement stmt = connection.createStatement();
        stmt.execute("CREATE SCHEMA IF NOT EXISTS " + lbSchema);
        stmt.execute("CREATE SCHEMA IF NOT EXISTS " + dbSchema);

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(dbSchema);
        database.setLiquibaseSchemaName(lbSchema);

        new CommandScope(UpdateCommandStep.COMMAND_NAME)
                .addArgumentValue("changeLogFile", lbChangeLog)
                .addArgumentValue("username", dbUsername)
                .addArgumentValue("password", dbPassword)
                .addArgumentValue("url", dbUrl)
                .addArgumentValue("database", database)
                .execute();

        System.out.println("Migration is complete successfully");
    }
}
