package org.kharisov.services.databaseImpls;

import org.kharisov.auditshared.annotations.Audit;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.*;
import org.kharisov.repos.interfaces.AuthRepo;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Класс AuthDbService представляет собой службу для работы с аутентификацией и авторизацией в базе данных.
 * Он реализует интерфейс AuthService и использует репозитории UserDbRepo и RoleDbRepo для выполнения операций с базой данных.
 */
@Service
public class AuthDbService implements AuthService {
    /**
     * Репозиторий для работы с пользователями.
     */
    private final AuthRepo authRepo;

    @Autowired
    public AuthDbService(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     *
     * @param user Пользователь.
     * @return true, если поvльзователь существует, иначе false.
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
     * @throws EntityNotFoundException Если пользователь не существует.
     */
    @Override
    public UserRecord getUserByAccountNum(String accountNum) throws MyDatabaseException, EntityNotFoundException {
        Optional<UserRecord> userRecordOptional = authRepo.getUserByAccountNum(accountNum);
        if (userRecordOptional.isEmpty())
            throw new EntityNotFoundException("The user was not found");
        return userRecordOptional.get();
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Объект пользователя.
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     */
    @Override
    public UserRecord getUserById(Long id) throws MyDatabaseException {
        Optional<UserRecord> userRecordOptional = authRepo.getUserById(id);
        if (userRecordOptional.isEmpty())
            throw new EntityNotFoundException("The user was not found");
        return userRecordOptional.get();
    }

    /**
     * Добавляет нового пользователя.
     *
     * @param user Объект пользователя для добавления.
     * @return Объект добавленного пользователя или пустой Optional, если добавление не удалось.
     */
    @Override
    public UserRecord addUser(UserRecord user) throws MyDatabaseException, ConflictException {

        if (userExistsByAccountNum(user)) {
            throw new ConflictException("The user already exists");
        }

        user = new UserRecord(
                null,
                user.accountNum(),
                AuthUtils.hashPassword(user.password()),
                user.role()
        );

        return authRepo.addUser(user).get();

    }

    /**
     * Проверяет, совпадает ли указанный пароль с паролем пользователя, найденным по номеру счета.
     *
     * @param user Объект UserRecord, представляющий пользователя.
     * @return UserRecord, представляющий пользователя
     */
    @Override
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
        return userRecordOptional.filter(userRecord -> Objects.equals(userRecord.role(), Role.ADMIN.name())).isPresent();
    }

    /**
     * Изменяет роль пользователя.
     *
     * @param user Пользователь, для которого требуется изменить роль.
     * @param role Новая роль, которую нужно установить для пользователя.
     */
    @Override
    @Audit(action="changeUserRole")
    public void changeUserRole(UserRecord user, Role role) throws MyDatabaseException {
        if (!authRepo.changeRoleByAccountNum(user.accountNum(), role)) {
            throw new EntityNotFoundException("The user was not found");
        }
    }
}
