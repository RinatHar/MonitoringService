package org.kharisov.configs;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс ConnectionPoolConfig представляет собой конфигурацию для пула соединений с базой данных.
 */
@Getter
@Setter
public class ConnectionPoolConfig {
    /**
     * URL базы данных, включающий схему базы данных.
     */
    private String url = Config.get("database.url") + "?currentSchema=" + Config.get("database.schema");

    /**
     * Имя пользователя для подключения к базе данных.
     */
    private String user = Config.get("database.username");

    /**
     * Пароль для подключения к базе данных.
     */
    private String password = Config.get("database.password");

    /**
     * Размер пула соединений с базой данных.
     */
    private int size = Integer.parseInt(Config.get("database.sizePool"));
}