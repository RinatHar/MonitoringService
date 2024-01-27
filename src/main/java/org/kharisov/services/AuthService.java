package org.kharisov.services;

import org.kharisov.entities.User;
import org.kharisov.repositories.UserRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    public Optional<User> addUser(User user) {
        user.setAccountNum(user.getAccountNum().strip());
        if (user.getAccountNum().length() == 16
                && user.getAccountNum().matches("\\d+")
                && user.getPassword().length() > 7) {
            user.setPassword(hashPassword(user.getPassword()));
            return Optional.ofNullable(userRepo.addUser(user));
        }
        else {
            return Optional.empty();
        }
    }

    public boolean logIn(String accountNum, String password) {
        User user = getUserByAccountNum(accountNum);
        return user != null && user.getPassword().equals(hashPassword(password));
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            return password;
        }
    }

    public boolean isAdminByAccountNum(String accountNum) {
        return getUserByAccountNum(accountNum).isAdmin();
    }

}
