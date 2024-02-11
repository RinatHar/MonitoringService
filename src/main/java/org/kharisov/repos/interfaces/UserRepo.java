package org.kharisov.repos.interfaces;

import org.kharisov.domains.User;

import java.util.*;

/**
 * Интерфейс UserRepo представляет контракт для репозитория пользователей.
 */
public interface UserRepo {

    /**
     * Записывает пользователя в хранилище и возвращает его.
     * @param user Объект пользователя.
     * @return Объект пользователя.
     */
    Optional<User> addUser(User user);

    /**
     * Возвращает пользователя из хранилища по его счету.
     * @param accountNum Счет пользователя.
     * @return Объект пользователя.
     */
    Optional<User> getUser(String accountNum);

    /**
     * Возвращает всех пользователя из хранилища.
     * @return Map, где ключ - счет пользователя, значение - объект пользователя.
     */
    Map<String, User> getAllUsers();

}

