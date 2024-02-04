package org.kharisov.repos.databaseImpls;

import org.kharisov.configs.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public abstract class BaseDbRepo<K, V> {
    private static final String DB_URL = Config.get("database.url") + "?currentSchema=" + Config.get("database.schema");
    private static final String DB_USERNAME = Config.get("database.username");
    private static final String DB_PASSWORD = Config.get("database.password");

    protected final Connection connection;

    public BaseDbRepo() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to the database", e);
        }
    }
    /**
     * Записывает объект в хранилище и возвращает его.
     * @param dto Объект DTO.
     * @return Объект DTO.
     */
    public abstract Optional<V> add(V dto);

    /**
     * Возвращает все объекты из хранилища.
     * @return Коллекция объектов DTO.
     */
    public abstract List<V> getAll();

}
