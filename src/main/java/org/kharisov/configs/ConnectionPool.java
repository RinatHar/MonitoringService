package org.kharisov.configs;

import java.sql.*;
import java.util.*;

/**
 * Класс ConnectionPool представляет собой пул соединений с базой данных.
 * Он управляет созданием, использованием и возвратом соединений с базой данных.
 */
public class ConnectionPool {
    private final List<Connection> connectionPool;
    private final String url;
    private final String user;
    private final String password;
    private final int MAX_POOL_SIZE;

    public ConnectionPool(ConnectionPoolConfig config) {
        this.url = config.getUrl();
        this.user = config.getUser();
        this.password = config.getPassword();
        this.connectionPool = new ArrayList<>(config.getSize());
        this.MAX_POOL_SIZE = config.getSize();
        this.initializeConnectionPool();
    }

    /**
     * Инициализирует пул соединений, создавая новые соединения до тех пор, пока пул не будет заполнен.
     */
    private void initializeConnectionPool() {
        while (!checkIfConnectionPoolIsFull()) {
            connectionPool.add(createNewConnectionForPool());
        }
    }

    /**
     * Проверяет, заполнен ли пул соединений.
     *
     * @return true, если пул соединений заполнен, иначе false.
     */
    private synchronized boolean checkIfConnectionPoolIsFull() {
        return connectionPool.size() >= MAX_POOL_SIZE;
    }

    /**
     * Создает новое соединение для пула.
     *
     * @return Новое соединение с базой данных.
     */
    private Connection createNewConnectionForPool() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating new connection", e);
        }
        return connection;
    }

    /**
     * Получает соединение из пула.
     *
     * @return Соединение с базой данных из пула или null, если пул пуст.
     */
    public synchronized Connection getConnectionFromPool() {
        Connection connection = null;
        if (connectionPool.size() > 0) {
            connection = connectionPool.get(0);
            connectionPool.remove(0);
        }
        return connection;
    }
    /**
     * Возвращает соединение обратно в пул.
     *
     * @param connection Соединение с базой данных, которое нужно вернуть в пул.
     */
    public synchronized void returnConnectionToPool(Connection connection) {
        connectionPool.add(connection);
    }
}