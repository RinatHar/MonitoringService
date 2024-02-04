package org.kharisov.repos.memoryImpls;

import org.kharisov.entities.*;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.storages.UserMemoryStorage;

import java.util.*;

/**
 * Класс UserMemoryRepo реализует интерфейс UserRepo.
 * Для взаимодействия с хранилищем пользователей в памяти.
 */
public class UserMemoryRepo implements UserRepo {

    /**
     * Хранилище для пользователей в памяти.
     */
    private final UserMemoryStorage userStorage;

    /**
     * Конструктор класса UserMemoryRepo.
     * @param userStorage Хранилище для пользователей в памяти.
     */
    public UserMemoryRepo(UserMemoryStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Записывает пользователя в хранилище в памяти и возвращает его.
     *
     * @param user Объект пользователя.
     * @return Объект пользователя.
     */
    @Override
    public Optional<User> addUser(User user) {
        userStorage.getStorage().put(user.getAccountNum(), user);
        return Optional.of(user);
    }

    /**
     * Возвращает пользователя из хранилища в памяти по его счету.
     *
     * @param accountNum Счет пользователя.
     * @return Объект пользователя.
     */
    @Override
    public Optional<User> getUser(String accountNum) {
        return Optional.ofNullable(userStorage.getStorage().get(accountNum));
    }

    /**
     * Возвращает всех пользователя из хранилища в памяти.
     * @return Map, где ключ - счет пользователя, значение - объект пользователя.
     */
    @Override
    public Map<String, User> getAllUsers() {
        return userStorage.getStorage();
    }
}
