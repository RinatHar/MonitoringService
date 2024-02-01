package org.kharisov.in.controllers;

import org.kharisov.entities.User;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.services.memoryImpls.AuthMemoryService;

import java.util.Optional;

/**
 * Класс AuthController представляет контроллер для аутентификации и авторизации пользователей.
 * Этот класс предоставляет методы для регистрации, входа в систему, назначения пользователя администратором и проверки административных прав.
 */
public class AuthController {
    /**
     * Сервис для работы с аутентификацией и авторизацией пользователей.
     */
    private final AuthService authService;

    /**
     * Конструктор класса AuthController.
     * @param authService Сервис для работы с аутентификацией и авторизацией пользователей.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Регистрирует нового пользователя с указанным номером счета и паролем.
     * @param accountNum Номер счета нового пользователя.
     * @param pass Пароль нового пользователя.
     * @return Optional<User>, содержащий нового пользователя, если регистрация прошла успешно, иначе Optional.empty().
     */
    public Optional<User> register(String accountNum, String pass) {
        if (authService.userExists(accountNum)) {
            return Optional.empty();
        } else {
            User newUser = User
                    .builder()
                    .accountNum(accountNum)
                    .password(pass)
                    .build();
            return authService.addUser(newUser);
        }
    }

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     * @param accountNum Номер счета пользователя.
     * @param pass Пароль пользователя.
     * @return Optional<User>, содержащий пользователя, если вход в систему прошел успешно, иначе Optional.empty().
     */
    public Optional<User> login(String accountNum, String pass) {
        if (authService.logIn(accountNum, pass)) {
            return Optional.ofNullable(authService.getUserByAccountNum(accountNum));
        }
        return Optional.empty();
    }

    /**
     * Назначает пользователя администратором.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь был успешно назначен администратором, иначе false.
     */
    public boolean makeUserAdmin(String accountNum) {
        if (authService.userExists(accountNum)) {
            authService.getUserByAccountNum(accountNum).setAdmin(true);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Добавляет нового администратора с указанным номером счета и паролем.
     * @param accountNum Номер счета нового администратора.
     * @param password Пароль нового администратора.
     */
    public void addAdmin(String accountNum, String password) {
        User admin = User
                .builder()
                .accountNum(accountNum)
                .password(password)
                .isAdmin(true)
                .build();
        authService.addUser(admin);
    }

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdminByAccountNum(String accountNum) {
        return authService.isAdminByAccountNum(accountNum);
    }
}