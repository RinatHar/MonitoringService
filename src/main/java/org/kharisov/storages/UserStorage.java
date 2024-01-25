package org.kharisov.storages;

import org.kharisov.entities.User;

import java.util.HashMap;

public class UserStorage {
    private final HashMap<String, User> userStorage = new HashMap<>();

    public UserStorage() {
    }

    public HashMap<String, User> getUserStorage() {
        return userStorage;
    }
}
