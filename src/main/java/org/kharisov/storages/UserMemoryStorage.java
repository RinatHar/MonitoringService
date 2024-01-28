package org.kharisov.storages;

import org.kharisov.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс UserMemoryStorage представляет реализацию хранилища пользователей в памяти.
 * Этот класс использует Map для хранения объектов User.
 */
public class UserMemoryStorage {

    /**
     * Хранилище пользователей.
     * Ключ - номер счета, значение - объект User.
     */
    private final Map<String, User> userStorage = new HashMap<>();

    /**
     * Конструктор по умолчанию.
     */
    public UserMemoryStorage() {
    }

    /**
     * Получить хранилище пользователей.
     * @return Map, где ключ - номер счета в виде строки, а значение - объект User.
     */
    public Map<String, User> getStorage() {
        return userStorage;
    }
}
