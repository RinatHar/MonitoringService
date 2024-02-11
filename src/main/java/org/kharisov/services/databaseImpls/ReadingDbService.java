package org.kharisov.services.databaseImpls;

import org.kharisov.annotations.*;
import org.kharisov.domains.*;
import org.kharisov.dtos.db.*;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.interfaces.ReadingService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс ReadingDbService представляет собой службу для работы с чтениями в базе данных.
 * Он реализует интерфейс ReadingService и использует репозитории ReadingDbRepo, UserDbRepo и ReadingTypeDbRepo для выполнения операций с базой данных.
 */
public class ReadingDbService implements ReadingService {
    /**
     * Репозиторий для работы с показаниями.
     */
    private final ReadingDbRepo readingDbRepo;
    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserDbRepo userDbRepo;
    /**
     * Репозиторий для работы с типами показаний.
     */
    private final ReadingTypeDbRepo readingTypeDbRepo;

    /**
     * Конструктор класса ReadingDbService.
     *
     * @param readingDbRepo Репозиторий для работы с показаниями.
     * @param userDbRepo Репозиторий для работы с пользователями.
     * @param readingTypeDbRepo Репозиторий для работы с типами показаний.
     */
    public ReadingDbService(ReadingDbRepo readingDbRepo,
                            UserDbRepo userDbRepo,
                            ReadingTypeDbRepo readingTypeDbRepo) {
        this.readingDbRepo = readingDbRepo;
        this.userDbRepo = userDbRepo;
        this.readingTypeDbRepo = readingTypeDbRepo;
    }

    /**
     * Добавляет новое чтение для указанного пользователя.
     *
     * @param user Пользователь, для которого добавляется показание.
     * @param reading Тип показания.
     * @param value Значение показания.
     * @return true, если успешно добавлено, иначе false.
     */
    @Override
    @Audit(action = "addReading")
    public boolean addReading(User user, ReadingType reading, int value) {
        ReadingDto readingDto = new ReadingDto();
        Optional<UserDto> optionalUserDto = userDbRepo.getByAccountNum(user.getAccountNum());
        Optional<ReadingTypeDto> optionalReadingTypeDto = readingTypeDbRepo.getByName(reading.getValue());
        if (optionalUserDto.isPresent() &&
                optionalReadingTypeDto.isPresent()) {
            UserDto userDto = optionalUserDto.get();
            ReadingTypeDto readingTypeDto = optionalReadingTypeDto.get();

            readingDto.setUserId(userDto.getId());
            readingDto.setTypeId(readingTypeDto.getId());
            readingDto.setValue(value);
            readingDto.setDate(LocalDate.now());

            return readingDbRepo.add(readingDto).isPresent();
        }
        return false;
    }

    /**
     * Проверяет, существует ли показание для указанного пользователя, типа показания и даты.
     *
     * @param user Пользователь, для которого проверяется показание.
     * @param reading Тип показания.
     * @param now Дата показания.
     * @return true, если чтение существует, иначе false.
     */
    @Override
    public boolean readingExists(User user, ReadingType reading, LocalDate now) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream().anyMatch(r -> r.getType().equals(reading) &&
                r.getDate().getYear() == now.getYear() &&
                r.getDate().getMonth() == now.getMonth());
    }

    /**
     * Возвращает все показания для указанного пользователя за указанный месяц и год.
     *
     * @param user Пользователь, для которого требуется получить показания.
     * @param month Месяц, за который требуется получить показания.
     * @param year Год, за который требуется получить показания.
     * @return Список записей показаний за указанный месяц и год.
     */
    @Override
    @Audit(action = "getReadingsByMonth")
    public List<ReadingRecord> getReadingsByMonth(User user, int month, int year) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream()
                .filter(r -> r.getDate().getMonthValue() == month && r.getDate().getYear() == year)
                .map(r -> ReadingRecord.builder()
                        .accountNum(r.getAccountNum())
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Возвращает текущее показание для указанного пользователя и типа показания.
     *
     * @param user Пользователь, для которого требуется получить показание.
     * @param type Тип показания.
     * @return Запись текущего показания или пустой Optional, если показание не найдено.
     */
    @Override
    @Audit(action = "getCurrentReading")
    public Optional<ReadingRecord> getCurrentReading(User user, ReadingType type) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream()
                .filter(r -> r.getType().equals(type))
                .max(Comparator.comparing(ReadingDto::getDate))
                .map(r -> ReadingRecord.builder()
                        .accountNum(r.getAccountNum())
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build());
    }

    /**
     * Возвращает историю показаний для указанного пользователя.
     *
     * @param user Пользователь, для которого требуется получить историю показаний.
     * @return Список записей всех показаний пользователя.
     */
    @Override
    @Audit(action = "getHistory")
    public List<ReadingRecord> getHistory(User user) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream()
                .map(r -> ReadingRecord.builder()
                        .accountNum(r.getAccountNum())
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Возвращает все показания из базы данных.
     *
     * @return Map, где ключ - это номер счета, а значение - это список записей показаний.
     */
    @Override
    @Audit(action = "getAllReadings")
    @Loggable
    public Map<String, List<ReadingRecord>> getAllReadings() {
        List<ReadingDto> readings = readingDbRepo.getAll();
        return readings.stream()
                .map(r -> ReadingRecord.builder()
                        .accountNum(r.getAccountNum())
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build())
                .collect(Collectors.groupingBy(ReadingRecord::getAccountNum));
    }
}
