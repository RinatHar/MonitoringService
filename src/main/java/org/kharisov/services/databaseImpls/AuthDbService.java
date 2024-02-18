package org.kharisov.services.databaseImpls;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.kharisov.annotations.Audit;
import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.*;
import org.kharisov.repos.interfaces.AuthRepo;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.AuthUtils;

import java.util.*;

/**
 * Класс AuthDbService представляет собой службу для работы с аутентификацией и авторизацией в базе данных.
 * Он реализует интерфейс AuthService и использует репозитории UserDbRepo и RoleDbRepo для выполнения операций с базой данных.
 */
@RequiredArgsConstructor
public class AuthDbService implements AuthService {
    /**
     * Репозиторий для работы с пользователями.
     */
    private final AuthRepo authRepo;

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     *
     * @param user Пользователь.
     * @return true, если пользователь существует, иначе false.
     */
    @Override
    public boolean userExistsByAccountNum(UserRecord user) throws MyDatabaseException {
        return authRepo.getUserByAccountNum(user.accountNum()).isPresent();
    }

    /**
     * Возвращает пользователя по его номеру счета.
     *
     * @param accountNum Номер счета пользователя.
     * @return Объект пользователя.
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     */
    @Override
    public Optional<UserRecord> getUserByAccountNum(String accountNum) throws MyDatabaseException {
        return authRepo.getUserByAccountNum(accountNum);
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Объект пользователя.
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     */
    @Override
    public Optional<UserRecord> getUserById(Long id) throws MyDatabaseException {
        return authRepo.getUserById(id);
    }

    /**
     * Возвращает пользователя, если он найден.
     *
     * @param userRecordOptional Запись с пользователем в бд.
     * @return Объект пользователя или пустой Optional, если пользователь не найден.
     */
    @NotNull
    private Optional<UserRecord> getUserRecordIfExists(Optional<UserRecord> userRecordOptional) throws MyDatabaseException {
        if (userRecordOptional.isPresent()) {
            UserRecord user = userRecordOptional.get();
            Optional<RoleRecord> roleRecordOptional = authRepo.getRoleById(user.role_id());
            if (roleRecordOptional.isPresent()) {
                return Optional.of(new UserRecord(
                        user.id(),
                        user.accountNum(),
                        user.password(),
                        roleRecordOptional.get().id()
                ));
            }
        }
        return Optional.empty();
    }

    /**
     * Добавляет нового пользователя.
     *
     * @param user Объект пользователя для добавления.
     * @return Объект добавленного пользователя или пустой Optional, если добавление не удалось.
     */
    @Override
    @Audit(action = "register")
    public Optional<UserRecord> addUser(UserRecord user) throws MyDatabaseException {
        if (AuthUtils.isValid(user)) {
            Optional<RoleRecord> roleRecordOptional = authRepo.getRoleById(user.role_id());
            if (roleRecordOptional.isPresent()) {
                RoleRecord roleRecord = roleRecordOptional.get();
                user = new UserRecord(
                        null,
                        user.accountNum(),
                        AuthUtils.hashPassword(user.password()),
                        roleRecord.id()
                );

                Optional<UserRecord> userRecordOptional = authRepo.addUser(user);
                if (userRecordOptional.isPresent()) {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Проверяет, совпадает ли указанный пароль с паролем пользователя, найденным по номеру счета.
     *
     * @param user Объект UserRecord, представляющий пользователя.
     * @return UserRecord, представляющий пользователя
     */
    @Override
    @Audit(action = "logIn")
    public UserRecord logIn(UserRecord user) throws MyDatabaseException {
        Optional<UserRecord> userRecordOptional = authRepo.getUserByAccountNum(user.accountNum());
        if (userRecordOptional.isPresent()) {
            UserRecord existingUser = userRecordOptional.get();
            if (!AuthUtils.checkPassword(user.password(), existingUser.password())) {
                throw new UnauthorizedException("Invalid password");
            }
            return existingUser;
        } else {
            throw new UnauthorizedException("The user was not found");
        }
    }

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     *
     * @param user Объект UserRecord, представляющий пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    @Override
    public boolean isAdmin(UserRecord user) throws MyDatabaseException {
        Optional<UserRecord> userRecordOptional = authRepo.getUserByAccountNum(user.accountNum());
        if (userRecordOptional.isPresent()) {
            user = userRecordOptional.get();
            Optional<RoleRecord> roleRecordOptional = authRepo.getRoleByName(Role.ADMIN);
            if (roleRecordOptional.isPresent()) {
                RoleRecord roleRecord = roleRecordOptional.get();
                return Objects.equals(user.role_id(), roleRecord.id());
            }
        }
        return false;
    }

    /**
     * Изменяет роль пользователя.
     *
     * @param user Пользователь, для которого требуется изменить роль.
     * @param role Новая роль, которую нужно установить для пользователя.
     */
    @Override
    @Audit(action = "changeUserRole")
    public void changeUserRole(UserRecord user, Role role) throws MyDatabaseException {
        RoleRecord roleRecord = new RoleRecord(
                null,
                role.name()
        );
        Optional<RoleRecord> roleDtoOptional = authRepo.changeRoleByAccountNum(
                user.accountNum(),
                roleRecord);
        if (roleDtoOptional.isEmpty())
            throw new EntityNotFoundException("The user was not found");
    }

    /**
     * Получаем роль по её идентификатору.
     *
     * @param id Идентификатор роли.
     * @return Роль.
     */
    @Override
    public Role getRoleById(Long id) throws MyDatabaseException {
        Optional<RoleRecord> role = authRepo.getRoleById(id);
        if (role.isEmpty())
            throw new EntityNotFoundException("The role was not found");
        return Role.valueOf(role.get().name());
    }

    /**
     * Получаем идентификатор роли по названию.
     *
     * @param role Роль, которую нужно получить.
     * @return Идентификатор роли, если роль существует.
     */
    @Override
    public Long getRoleIdByName(Role role) throws MyDatabaseException {
        Optional<RoleRecord> record = getRoleByName(role);

        Long roleId;

        if (record.isPresent()) {
            roleId = record.get().id();
        } else {
            throw new EntityNotFoundException("The role was not found");
        }
        return roleId;
    }

    /**
     * Получаем роль по названию.
     *
     * @param role Роль, которую нужно получить.
     * @return roleDtoOptional, если роль существует, иначе пустой Optional.
     */
    @Override
    public Optional<RoleRecord> getRoleByName(Role role) throws MyDatabaseException {
        return authRepo.getRoleByName(role);
    }
}
