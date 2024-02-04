package org.kharisov.services.memoryImpls;

import org.kharisov.entities.User;
import org.kharisov.enums.Role;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.AuthUtils;

import java.util.*;

/**
 * Класс AuthService представляет сервис для аутентификации и авторизации пользователей.
 * Этот класс предоставляет методы для проверки существования пользователя, добавления пользователя, входа в систему и проверки административных прав.
 */
public class AuthMemoryService implements AuthService {
    /**
     * Репозиторий для управления хранилищем пользователей.
     */
    private final UserRepo userRepo;

    /**
     * Конструктор класса AuthService.
     * @param userRepo Репозиторий для управления хранилищем пользователей.
     */
    public AuthMemoryService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    public boolean userExists(String accountNum) {
        Optional<User> user = getUserByAccountNum(accountNum);
        return user.isPresent();
    }

    /**
     * Получает пользователя по номеру счета.
     * @param accountNum Номер счета пользователя.
     * @return Объект User, если пользователь существует, иначе null.
     */
    public Optional<User> getUserByAccountNum(String accountNum) {
        return userRepo.getUser(accountNum);
    }

    /**
     * Добавляет нового пользователя, если выполнены условия валидации.
     * @param user Объект User, представляющий нового пользователя.
     * @return Optional<User>, содержащий нового пользователя, если он был успешно добавлен, иначе Optional.empty().
     */
    public Optional<User> addUser(User user) {
        user.setAccountNum(user.getAccountNum().strip());
        if (AuthUtils.isValid(user)) {
            user.setPassword(AuthUtils.hashPassword(user.getPassword()));
            return userRepo.addUser(user);
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     * @param accountNum Номер счета пользователя.
     * @param password Пароль пользователя.
     * @return true, если пользователь может войти в систему, иначе false.
     */
    public boolean logIn(String accountNum, String password) {
        Optional<User> user = getUserByAccountNum(accountNum);
        return user.isPresent() && AuthUtils.checkPassword(password, user.get().getPassword());
    }

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdminByAccountNum(String accountNum) {
        Optional<User> user = getUserByAccountNum(accountNum);
        if (user.isPresent()) {
            user.get().isAdmin();
            return true;
        }
        return false;


    }

    /**
     * Изменяет роль пользователя.
     * @param user Объект User, представляющий пользователя.
     * @param role Объект Role, представляющий новую роль для пользователя.
     * @return true, если роль успешно изменена, иначе false.
     */
    @Override
    public boolean changeUserRole(User user, Role role) {
        user.setRole(role);
        return true;
    }
}