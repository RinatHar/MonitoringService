package org.kharisov.repos.interfaces;

import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.MyDatabaseException;

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
    Optional<UserRecord> addUser(UserRecord record) throws MyDatabaseException;

    /**
     * Возвращает пользователя по его номеру счета.
     *
     * @param accountNum Номер счета пользователя, которого требуется получить.
     * @return Объект сущности пользователя с указанным номером счета или пустой Optional, если такого пользователя не существует.
     */
    Optional<UserRecord> getUserByAccountNum(String accountNum) throws MyDatabaseException;

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя, которого требуется получить.
     * @return Объект сущности пользователя с указанным идентификатором или пустой Optional,
     * если такого пользователя не существует.
     */
    Optional<UserRecord> getUserById(Long id) throws MyDatabaseException;

    /**
     * Возвращает всех пользователей.
     *
     * @return Список всех объектов сущности пользователей.
     */
    List<UserRecord> getAllUsers() throws MyDatabaseException;

    /**
     * Добавляет роль и возвращает ее.
     *
     * @param record Объект сущности роли для добавления.
     * @return Объект сущности добавленной роли или пустой Optional, если добавление не удалось.
     */
    Optional<RoleRecord> addRole(RoleRecord record) throws MyDatabaseException;

    /**
     * Возвращает роль по ее идентификатору.
     *
     * @param id Идентификатор роли, которую требуется получить.
     * @return Объект сущности роли с указанным идентификатором или пустой Optional, если такой роли не существует.
     */
    Optional<RoleRecord> getRoleById(Long id) throws MyDatabaseException;

    /**
     * Возвращает роль по ее имени.
     *
     * @param role Роль, которую требуется получить.
     * @return Объект сущности роли с указанным именем или пустой Optional, если такой роли не существует.
     */
    Optional<RoleRecord> getRoleByName(Role role) throws MyDatabaseException;

    /**
     * Изменяет роль пользователя по номеру счета.
     *
     * @param accountNum Номер счета пользователя, для которого требуется изменить роль.
     * @param record Объект сущности роли, представляет новую роль, которую нужно установить.
     * @return Объект сущности обновленной роли или пустой Optional, если обновление не удалось.
     */
    Optional<RoleRecord> changeRoleByAccountNum(String accountNum, RoleRecord record) throws MyDatabaseException;

    /**
     * Возвращает все роли пользователей.
     *
     * @return Список всех объектов сущности ролей.
     */
    List<RoleRecord> getAllRoles() throws MyDatabaseException;
}
