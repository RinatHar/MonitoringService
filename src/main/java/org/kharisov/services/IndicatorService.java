package org.kharisov.services;

import org.kharisov.entities.IndicatorRecord;
import org.kharisov.entities.User;
import org.kharisov.enums.IndicatorTypeEnum;
import org.kharisov.repositories.UserRepo;

import java.time.LocalDate;
import java.util.*;

public class IndicatorService {
    private final UserRepo userRepo;

    public IndicatorService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean addIndicator(User user, IndicatorTypeEnum indicator, int value) {
        LocalDate now = LocalDate.now();
        if (!ifIndicatorExists(user, indicator, now)) {
            IndicatorRecord record = IndicatorRecord.builder()
                    .type(indicator)
                    .value(value)
                    .date(now)
                    .build();
            user.getIndicators().add(record);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean ifIndicatorExists(User user, IndicatorTypeEnum indicator, LocalDate now) {
        return user.getIndicators().stream()
                .anyMatch(record -> record.getType() == indicator
                        && record.getDate().getMonth() == now.getMonth()
                        && record.getDate().getYear() == now.getYear());
    }
    public IndicatorRecord getIndicatorByType(User user, IndicatorTypeEnum type, int month, int year) {
        return user.getIndicators().stream()
                .filter(record -> record.getType() == type
                        && record.getDate().getMonthValue() == month
                        && record.getDate().getYear() == year)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Не найдены показания за данный месяц и год"));
    }

    public Optional<IndicatorRecord> getCurrentIndicator(User user, IndicatorTypeEnum type) {
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
