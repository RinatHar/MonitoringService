package org.kharisov.services.interfaces;

import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.enums.Role;

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
     * @return Объект UserRecord.
     */
    UserRecord getUserByAccountNum(String accountNum);

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Объект пользователя.
     */
    UserRecord getUserById(Long id);

    /**
     * Добавляет нового пользователя, если выполнены условия валидации.
     * @param user Объект UserRecord, представляющий нового пользователя.
     * @return UserRecord, содержащий нового пользователя.
     */
    UserRecord addUser(UserRecord user);

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
}
