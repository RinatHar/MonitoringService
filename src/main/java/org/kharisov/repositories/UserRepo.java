package org.kharisov.repositories;

import org.kharisov.entities.User;

import java.util.Map;

public abstract class UserRepo {

    public abstract User addUser(User user);

    public abstract User getUser(String accountNum);

    public abstract Map<String, User> getAllUsers();

}

