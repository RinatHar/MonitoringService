package org.kharisov.in.controllers;

import org.kharisov.entities.User;
import org.kharisov.services.AuthService;

import java.util.Optional;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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

    public Optional<User> login(String accountNum, String pass) {
        if (authService.logIn(accountNum, pass)) {
            return Optional.ofNullable(authService.getUserByAccountNum(accountNum));
        }
        return Optional.empty();
    }

    public boolean makeUserAdmin(String accountNum) {
        if (authService.userExists(accountNum)) {
            authService.getUserByAccountNum(accountNum).setAdmin(true);
            return true;
        }
        else {
            return false;
        }
    }

    public void addAdmin(String accountNum, String password) {
        User admin = User
                .builder()
                .accountNum(accountNum)
                .password(password)
                .isAdmin(true)
                .build();
        authService.addUser(admin);
    }

    public boolean isAdminByAccountNum(String accountNum) {
        return authService.isAdminByAccountNum(accountNum);
    }

}
