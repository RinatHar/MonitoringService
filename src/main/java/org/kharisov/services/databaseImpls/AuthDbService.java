package org.kharisov.services.databaseImpls;

import org.kharisov.annotations.Audit;
import org.kharisov.domains.User;
import org.kharisov.dtos.db.*;
import org.kharisov.enums.Role;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.AuthUtils;

import java.util.*;

/**
 * Класс AuthDbService представляет собой службу для работы с аутентификацией и авторизацией в базе данных.
 * Он реализует интерфейс AuthService и использует репозитории UserDbRepo и RoleDbRepo для выполнения операций с базой данных.
 */
public class AuthDbService implements AuthService {
    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserDbRepo userDbRepo;
    /**
     * Репозиторий для работы с ролями.
     */
    private final RoleDbRepo roleDbRepo;

    /**
     * Конструктор класса AuthDbService.
     *
     * @param userDbRepo Репозиторий для работы с пользователями.
     * @param roleDbRepo Репозиторий для работы с ролями.
     */
    public AuthDbService(UserDbRepo userDbRepo, RoleDbRepo roleDbRepo) {
        this.userDbRepo = userDbRepo;
        this.roleDbRepo = roleDbRepo;
    }

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     *
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    @Override
    public boolean userExists(String accountNum) {
        return userDbRepo.getByAccountNum(accountNum).isPresent();
    }

    /**
     * Возвращает пользователя по его номеру счета.
     *
     * @param accountNum Номер счета пользователя.
     * @return Объект пользователя или пустой Optional, если пользователь не найден.
     */
    @Override
    public Optional<User> getUserByAccountNum(String accountNum) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(accountNum);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            Optional<RoleDto> roleDtoOptional = roleDbRepo.getById(userDto.getRoleId());
            if (roleDtoOptional.isPresent()) {
                RoleDto roleDto = roleDtoOptional.get();
                return Optional.ofNullable(User.builder()
                        .accountNum(userDto.getAccountNum())
                        .password(userDto.getPassword())
                        .role(Role.valueOf(roleDto.getName()))
                        .build());
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
    public Optional<User> addUser(User user) {
        if (AuthUtils.isValid(user)) {
            Optional<RoleDto> roleDtoOptional = roleDbRepo.getByName(String.valueOf(user.getRole()));
            if (roleDtoOptional.isPresent()) {
                RoleDto roleDto = roleDtoOptional.get();
                UserDto userDto = new UserDto();
                userDto.setAccountNum(user.getAccountNum());
                userDto.setPassword(AuthUtils.hashPassword(user.getPassword()));
                userDto.setRoleId(roleDto.getId());

                Optional<UserDto> userDtoOptional = userDbRepo.add(userDto);
                if (userDtoOptional.isPresent()) {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Проверяет, совпадает ли указанный пароль с паролем пользователя, найденным по номеру счета.
     *
     * @param accountNum Номер счета пользователя.
     * @param password   Пароль для проверки.
     * @return true, если пароли совпадают, иначе false.
     */
    @Override
    @Audit(action = "logIn")
    public boolean logIn(String accountNum, String password) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(accountNum);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            return AuthUtils.checkPassword(password, userDto.getPassword());
        } else {
            return false;
        }
    }

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     *
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    @Override
    public boolean isAdminByAccountNum(String accountNum) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(accountNum);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            Optional<RoleDto> roleDtoOptional = roleDbRepo.getByName(String.valueOf(Role.ADMIN));
            if (roleDtoOptional.isPresent()) {
                RoleDto roleDto = roleDtoOptional.get();
                return Objects.equals(userDto.getRoleId(), roleDto.getId());
            }
        }
        return false;
    }

    /**
     * Изменяет роль пользователя.
     *
     * @param user Пользователь, для которого требуется изменить роль.
     * @param role Новая роль, которую нужно установить для пользователя.
     * @return true, если роль пользователя была успешно изменена, иначе false.
     */
    @Override
    @Audit(action = "changeUserRole")
    public boolean changeUserRole(User user, Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.name());
        Optional<RoleDto> roleDtoOptional = roleDbRepo.changeRoleByAccountNum(
                user.getAccountNum(),
                roleDto);
        return roleDtoOptional.isPresent();
    }
}
