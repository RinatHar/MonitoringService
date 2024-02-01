package org.kharisov.services.interfaces;

import org.kharisov.entities.User;

import java.util.Optional;

/**
 * Интерфейс AuthService представляет контракт для сервиса аутентификации.
 */
public interface AuthService {

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    public boolean userExists(String accountNum);

    /**
     * Получает пользователя по номеру счета.
     * @param accountNum Номер счета пользователя.
     * @return Объект User, если пользователь существует, иначе null.
     */
    public User getUserByAccountNum(String accountNum);

    /**
     * Добавляет нового пользователя, если выполнены условия валидации.
     * @param user Объект User, представляющий нового пользователя.
     * @return Optional<User>, содержащий нового пользователя, если он был успешно добавлен, иначе Optional.empty().
     */
    public Optional<User> addUser(User user);

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     * @param accountNum Номер счета пользователя.
     * @param password Пароль пользователя.
     * @return true, если пользователь может войти в систему, иначе false.
     */
    public boolean logIn(String accountNum, String password);

    /**
     * Метод для хеширования пароля с использованием соли.
     * @param password Пароль, который нужно захешировать.
     * @return Хеш пароля, сгенерированный с использованием соли. Если произошла ошибка, возвращает исходный пароль.
     */
    public String hashPassword(String password);

    /**
     * Метод для проверки пароля пользователя.
     * @param password Введенный пароль.
     * @param storedPasswordHash Хеш сохраненного пароля.
     * @return true, если введенный пароль соответствует сохраненному хешу пароля, иначе false.
     */
    public boolean checkPassword(String password, String storedPasswordHash);

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdminByAccountNum(String accountNum);
}
