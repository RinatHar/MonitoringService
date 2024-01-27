package org.kharisov.services;

import org.kharisov.entities.IndicatorRecord;
import org.kharisov.entities.User;
import org.kharisov.repositories.UserRepo;
import org.kharisov.storages.IndicatorType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class IndicatorService {
    private final UserRepo userRepo;

    public IndicatorService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addIndicator(User user, IndicatorType indicator, int value) {
        LocalDate now = LocalDate.now();
        if (!indicatorExists(user, indicator, now)) {
            IndicatorRecord record = IndicatorRecord.builder()
                    .type(indicator)
                    .value(value)
                    .date(now)
                    .build();
            user.getIndicators().add(record);
        }
    }

    public boolean indicatorExists(User user, IndicatorType indicator, LocalDate now) {
        return user.getIndicators().stream()
                .anyMatch(record -> record.getType() == indicator
                        && record.getDate().getMonth() == now.getMonth()
                        && record.getDate().getYear() == now.getYear());
    }
    public List<IndicatorRecord> getIndicatorsByMonth(User user, int month, int year) {
        return user.getIndicators().stream()
                .filter(record -> record.getDate().getMonthValue() == month
                        && record.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

    public Optional<IndicatorRecord> getCurrentIndicator(User user, IndicatorType type) {
        return user.getIndicators().stream()
                .filter(record -> record.getType() == type)
                .max(Comparator.comparing(IndicatorRecord::getDate));
    }
    public List<IndicatorRecord> getHistory(User user) {
        return user.getIndicators();
    }

    public Map<String, List<IndicatorRecord>> getAllIndicators() {
        Map<String, User> allUsers = userRepo.getAllUsers();
        Map<String, List<IndicatorRecord>> allIndicators = new HashMap<>();
        for (Map.Entry<String, User> entry : allUsers.entrySet()) {
            allIndicators.put(entry.getKey(), entry.getValue().getIndicators());
        }
        return allIndicators;
    }

}
