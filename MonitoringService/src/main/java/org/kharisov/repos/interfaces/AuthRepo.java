package org.kharisov.repos.interfaces;

import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.enums.Role;

import java.util.*;

/**
 * Класс UserDbRepo представляет собой контракт для работы с пользователями.
 */
public interface AuthRepo {

    /**
     * Добавляет пользователя и возвращает его.
     *
     * @param record Объект сущности пользователя для добавления.
     * @return Объект сущности добавленного пользователя или пустой Optional, если добавление не удалось.
     */
    Optional<UserRecord> addUser(UserRecord record);

    /**
     * Возвращает пользователя по его номеру счета.
     *
     * @param accountNum Номер счета пользователя, которого требуется получить.
     * @return Объект сущности пользователя с указанным номером счета или пустой Optional, если такого пользователя не существует.
     */
    Optional<UserRecord> getUserByAccountNum(String accountNum);

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя, которого требуется получить.
     * @return Объект сущности пользователя с указанным идентификатором или пустой Optional,
     * если такого пользователя не существует.
     */
    Optional<UserRecord> getUserById(Long id);

    /**
     * Возвращает всех пользователей.
     *
     * @return Список всех объектов сущности пользователей.
     */
    List<UserRecord> getAllUsers();

    /**
     * Изменяет роль пользователя по номеру счета.
     *
     * @param accountNum Номер счета пользователя, для которого требуется изменить роль.
     * @param role Роль которую нужно установить.
     * @return true если роль удалось изменить или пустой false, если обновление не удалось.
     */
    boolean changeRoleByAccountNum(String accountNum, Role role);
}
