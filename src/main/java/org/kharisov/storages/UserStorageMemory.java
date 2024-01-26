package org.kharisov.storages;

import org.kharisov.entities.User;

import java.util.HashMap;
import java.util.Map;

public class UserStorageMemory {
    private final HashMap<String, User> userStorage = new HashMap<>();

    public UserStorageMemory() {
    }

    public Map<String, User> getStorage() {
        return userStorage;
    }
}
