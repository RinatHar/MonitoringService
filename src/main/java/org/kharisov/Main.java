package org.kharisov;

import org.kharisov.entities.User;
import org.kharisov.enums.IndicatorEnum;
import org.kharisov.repos.UserRepo;
import org.kharisov.storages.UserStorage;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        UserStorage userStorage = new UserStorage();
        UserRepo userRepo = new UserRepo(userStorage);

        User user = User
                .builder()
                .accountNum("1")
                .password("123")
                .build();


        userRepo.addUser(user);

        boolean isSuc = userRepo.logIn("1", "123");

        userRepo.addIndicator(user, IndicatorEnum.HOTWATER, 5);
        userRepo.addIndicator(user, IndicatorEnum.COLDWATER, 10);

        int ind = userRepo.getIndicator(user, IndicatorEnum.HOTWATER);
        HashMap<IndicatorEnum, Integer> inds = userRepo.getAllIndicators(user);

        System.out.println(isSuc);
        System.out.println(ind);
        System.out.println(inds);
    }
}