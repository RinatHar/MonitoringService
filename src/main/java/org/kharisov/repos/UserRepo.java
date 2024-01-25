package org.kharisov.repos;

import org.kharisov.entities.User;
import org.kharisov.enums.IndicatorEnum;
import org.kharisov.storages.*;

import java.util.HashMap;

public class UserRepo {
    private final UserStorage userStorage;

    public UserRepo(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User getUserByAccountNum(String account_num) {
        return userStorage.getUserStorage().get(account_num);
    }

    public void addUser(User user) {
        if (!userStorage.getUserStorage().containsKey(user.getAccountNum())) {
            userStorage.getUserStorage().put(user.getAccountNum(), user);
        }
    }

    public boolean logIn(String account_num, String password) {
        User user = getUserByAccountNum(account_num);
        return user != null && user.getPassword().equals(password);
    }

    public void addIndicator(User user, IndicatorEnum indicator, int value) {
        user.getIndicators().put(indicator, value);
    }

    public int getIndicator(User user, IndicatorEnum indicator) {
        return user.getIndicators().get(indicator);
    }

    public HashMap<IndicatorEnum, Integer> getAllIndicators(User user) {
        return user.getIndicators();
    }
}
