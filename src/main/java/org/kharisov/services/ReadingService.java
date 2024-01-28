package org.kharisov.services;

import org.kharisov.entities.ReadingRecord;
import org.kharisov.entities.User;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.entities.ReadingType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс ReadingService представляет сервис для работы с показаниями.
 * Этот класс предоставляет методы для добавления показаний, проверки их существования, получения показаний по месяцу и году, получения текущего показания, истории показаний и всех показаний.
 */
public class ReadingService {
    /**
     * Репозиторий для управления хранилищем пользователей.
     */
    private final UserRepo userRepo;

    /**
     * Конструктор класса ReadingService.
     * @param userRepo Репозиторий для управления хранилищем пользователей.
     */
    public ReadingService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Добавляет показание для указанного пользователя, если оно еще не существует.
     * @param user Пользователь, для которого добавляется показание.
     * @param reading Тип показания.
     * @param value Значение показания.
     */
    public void addReading(User user, ReadingType reading, int value) {
        LocalDate now = LocalDate.now();
        if (!readingExists(user, reading, now)) {
            ReadingRecord record = ReadingRecord.builder()
                    .type(reading)
                    .value(value)
                    .date(now)
                    .build();
            user.getReadings().add(record);
        }
    }

    /**
     * Проверяет, существует ли указанное показание для пользователя на текущую дату.
     * @param user Пользователь, для которого проверяется показание.
     * @param reading Тип показания.
     * @param now Текущая дата.
     * @return true, если показание существует, иначе false.
     */
    public boolean readingExists(User user, ReadingType reading, LocalDate now) {
        return user.getReadings().stream()
                .anyMatch(record -> record.getType() == reading
                        && record.getDate().getMonth() == now.getMonth()
                        && record.getDate().getYear() == now.getYear());
    }

    /**
     * Получает показания указанного пользователя за указанный месяц и год.
     * @param user Пользователь, для которого получаются показания.
     * @param month Месяц, за который получаются показания.
     * @param year Год, за который получаются показания.
     * @return Список показаний чтения.
     */
    public List<ReadingRecord> getReadingsByMonth(User user, int month, int year) {
        return user.getReadings().stream()
                .filter(record -> record.getDate().getMonthValue() == month
                        && record.getDate().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Получает текущее показание чтения указанного типа для пользователя.
     * @param user Пользователь, для которого получается показание.
     * @param type Тип показания.
     * @return Optional<ReadingRecord>, содержащий текущее показание, если оно существует, иначе Optional.empty().
     */
    public Optional<ReadingRecord> getCurrentReading(User user, ReadingType type) {
        return user.getReadings().stream()
                .filter(record -> record.getType() == type)
                .max(Comparator.comparing(ReadingRecord::getDate));
    }

    /**
     * Получает историю показаний для указанного пользователя.
     * @param user Пользователь, для которого получается история показаний.
     * @return Список показаний.
     */
    public List<ReadingRecord> getHistory(User user) {
        return user.getReadings();
    }

    /**
     * Получает все показания всех пользователей.
     * @return Map, где ключом является номер счета пользователя, а значением - список показаний.
     */
    public Map<String, List<ReadingRecord>> getAllReadings() {
        Map<String, User> allUsers = userRepo.getAllUsers();
        Map<String, List<ReadingRecord>> allReadings = new HashMap<>();
        for (Map.Entry<String, User> entry : allUsers.entrySet()) {
            allReadings.put(entry.getKey(), entry.getValue().getReadings());
        }
        return allReadings;
    }
}
