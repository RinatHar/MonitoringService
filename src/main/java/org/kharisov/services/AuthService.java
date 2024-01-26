package org.kharisov.services;

import org.kharisov.entities.User;
import org.kharisov.repositories.UserRepo;

import java.util.Map;

public class AuthService {
    private final UserRepo userRepo;

    public AuthService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean userExists(String accountNum) {
        User user = getUserByAccountNum(accountNum);
        return user != null;
    }
    public User getUserByAccountNum(String accountNum) {
        return userRepo.getUser(accountNum);
    }

    public User addUser(User user) {
        return userRepo.addUser(user);
    }

    public Map<String, User> getUsers() {
        return userRepo.getAllUsers();
    }

    public boolean logIn(String accountNum, String password) {
        User user = getUserByAccountNum(accountNum);
        return user != null && user.getPassword().equals(password);
    }

}
