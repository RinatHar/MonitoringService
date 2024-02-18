package org.kharisov.services.interfaces;

import org.kharisov.domains.User;
import org.kharisov.enums.Role;

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
    boolean userExists(String accountNum);

    /**
     * Получает пользователя по номеру счета.
     * @param accountNum Номер счета пользователя.
     * @return Объект User, если пользователь существует, иначе null.
     */
    Optional<User> getUserByAccountNum(String accountNum);

    /**
     * Добавляет нового пользователя, если выполнены условия валидации.
     * @param user Объект User, представляющий нового пользователя.
     * @return Optional<User>, содержащий нового пользователя, если он был успешно добавлен, иначе Optional.empty().
     */
    Optional<User> addUser(User user);

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     * @param accountNum Номер счета пользователя.
     * @param password Пароль пользователя.
     * @return true, если пользователь может войти в систему, иначе false.
     */
    boolean logIn(String accountNum, String password);

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    boolean isAdminByAccountNum(String accountNum);

    /**
     * Изменяет роль пользователя.
     * @param user Объект User, представляющий пользователя.
     * @param role Объект Role, представляющий новую роль для пользователя.
     * @return true, если роль успешно изменена, иначе false.
     */
    boolean changeUserRole(User user, Role role);
}
