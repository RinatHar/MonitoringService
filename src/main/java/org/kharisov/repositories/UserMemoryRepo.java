package org.kharisov.repositories;

import org.kharisov.entities.*;
import org.kharisov.storages.UserStorageMemory;

import java.util.*;

public class UserMemoryRepo extends UserRepo {
    UserStorageMemory userStorage;

    public UserMemoryRepo(UserStorageMemory userStorage) {
        this.userStorage = userStorage;
    }
    @Override
    public User addUser(User user) {
        userStorage.getStorage().put(user.getAccountNum(), user);
        return user;
    }

    @Override
    public User getUser(String accountNum) {
        return userStorage.getStorage().get(accountNum);
    }

    @Override
    public Map<String, User> getAllUsers() {
        return userStorage.getStorage();
    }
}
