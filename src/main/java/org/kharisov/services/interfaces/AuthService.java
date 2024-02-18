package org.kharisov.services.interfaces;

import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.MyDatabaseException;

import java.util.Optional;

/**
 * Интерфейс AuthService представляет контракт для сервиса аутентификации.
 */
public interface AuthService {

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     * @param user Объект UserRecord, представляющий пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    boolean userExistsByAccountNum(UserRecord user);

    /**
     * Получает пользователя по номеру счета.
     *
     * @param accountNum Номер счета пользователя.
     * @return Объект UserRecord, если пользователь существует, иначе null.
     */
    Optional<UserRecord> getUserByAccountNum(String accountNum);

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Объект пользователя.
     */
    Optional<UserRecord> getUserById(Long id);

    /**
     * Добавляет нового пользователя, если выполнены условия валидации.
     * @param user Объект UserRecord, представляющий нового пользователя.
     * @return Optional<UserRecord>, содержащий нового пользователя, если он был успешно добавлен, иначе Optional.empty().
     */
    Optional<UserRecord> addUser(UserRecord user);

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     *
     * @param user Объект UserRecord, представляющий пользователя.
     * @return UserRecord, представляющий пользователя
     */
    UserRecord logIn(UserRecord user);

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     * @param user Объект UserRecord, представляющий пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    boolean isAdmin(UserRecord user);

    /**
     * Изменяет роль пользователя.
     * @param user Объект UserRecord, представляющий пользователя.
     * @param role Объект Role, представляющий новую роль для пользователя.
     */
    void changeUserRole(UserRecord user, Role role);

    /**
     * Получаем роль по её идентификатору.
     *
     * @param id Идентификатор роли.
     * @return Роль.
     */
    Role getRoleById(Long id);

    /**
     * Получаем идентификатор роли по названию.
     *
     * @param role Роль, которую нужно получить.
     * @return Идентификатор роли, если роль существует.
     */
    Long getRoleIdByName(Role role);

    /**
     * Получаем роль по названию.
     *
     * @param role Роль, которую нужно получить.
     * @return roleDtoOptional, если роль существует, иначе пустой Optional.
     */
    Optional<RoleRecord> getRoleByName(Role role);
}
