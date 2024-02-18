package org.kharisov.services.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.annotations.*;
import org.kharisov.entities.*;
import org.kharisov.exceptions.*;
import org.kharisov.repos.interfaces.*;
import org.kharisov.services.interfaces.ReadingService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс ReadingDbService представляет собой службу для работы с чтениями в базе данных.
 * Он реализует интерфейс ReadingService и использует репозитории ReadingDbRepo, UserDbRepo и ReadingTypeDbRepo для выполнения операций с базой данных.
 */
@RequiredArgsConstructor
public class ReadingDbService implements ReadingService {
    /**
     * Репозиторий для работы с показаниями.
     */
    private final ReadingRepo readingRepo;
    /**
     * Репозиторий для работы с пользователями.
     */
    private final AuthRepo authRepo;
    /**
     * Репозиторий для работы с типами показаний.
     */
    private final ReadingTypeRepo readingTypeRepo;

    /**
     * Добавляет новое чтение для указанного пользователя.
     *
     * @param user Пользователь, для которого добавляется показание.
     * @param readingTypeRecord Тип показания.
     * @param value Значение показания.
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     * @throws ConflictException Если показание за текущий месяц уже существует.
     * @throws EntityNotFoundException Если текущий пользователь или тип показания не найден.
     */
    @Override
    @Audit(action = "addReading")
    public void addReading(UserRecord user, ReadingTypeRecord readingTypeRecord, int value)
            throws MyDatabaseException, EntityNotFoundException, ConflictException {
        Optional<UserRecord> userRecordOptional = authRepo.getUserByAccountNum(user.accountNum());
        Optional<ReadingTypeRecord> readingTypeRecordOptional = readingTypeRepo.getByName(readingTypeRecord.name());
        if (userRecordOptional.isEmpty() ||
                readingTypeRecordOptional.isEmpty()) {
            throw new EntityNotFoundException("Текущий пользователь или тип показания не найден");
        }
        readingTypeRecord = readingTypeRecordOptional.get();
        ReadingRecord record = new ReadingRecord(
                null,
                user.id(),
                readingTypeRecord.id(),
                value,
                LocalDate.now()
        );

        if (readingExists(user, readingTypeRecord, LocalDate.now())) {
            throw new ConflictException("Показание за текущий месяц уже отправлено");
        }
        readingRepo.add(record);
    }

    /**
     * Проверяет, существует ли показание для указанного пользователя, типа показания и даты.
     *
     * @param user Пользователь, для которого проверяется показание.
     * @param readingTypeRecord Тип показания.
     * @param now Дата показания.
     * @return true, если чтение существует, иначе false.
     */
    @Override
    public boolean readingExists(UserRecord user, ReadingTypeRecord readingTypeRecord, LocalDate now) throws MyDatabaseException {
        List<UserReadingRecord> records = readingRepo.getAllByAccountNum(user.accountNum());
        return records.stream().anyMatch(r -> r.type().equals(readingTypeRecord.name()) &&
                r.date().getYear() == now.getYear() &&
                r.date().getMonth() == now.getMonth());
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
    public List<UserReadingRecord> getReadingsByMonth(UserRecord user, int month, int year) throws MyDatabaseException {
        List<UserReadingRecord> records = readingRepo.getAllByAccountNum(user.accountNum());
        return records.stream()
                .filter(r -> r.date().getMonthValue() == month && r.date().getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает текущее показание для указанного пользователя и типа показания.
     *
     * @param user Пользователь, для которого требуется получить показание.
     * @param readingTypeRecord Тип показания.
     * @return Запись текущего показания или пустой Optional, если показание не найдено.
     */
    @Override
    @Audit(action = "getCurrentReading")
    public UserReadingRecord getCurrentReading(UserRecord user, ReadingTypeRecord readingTypeRecord) throws MyDatabaseException {
        List<UserReadingRecord> records = readingRepo.getAllByAccountNum(user.accountNum());
        Optional<UserReadingRecord> currentReading = records.stream()
                .filter(r -> r.type().equals(readingTypeRecord.name()))
                .max(Comparator.comparing(UserReadingRecord::date));
        if (currentReading.isEmpty())
            throw new EntityNotFoundException("Показания не найдены");
        return currentReading.get();
    }

    /**
     * Возвращает историю показаний для указанного пользователя.
     *
     * @param user Пользователь, для которого требуется получить историю показаний.
     * @return Список записей всех показаний пользователя.
     */
    @Override
    @Audit(action = "getHistory")
    public List<UserReadingRecord> getHistory(UserRecord user) throws MyDatabaseException {
        return readingRepo.getAllByAccountNum(user.accountNum());
    }

    /**
     * Возвращает все показания из базы данных.
     *
     * @return Map, где ключ - это номер счета, а значение - это список записей показаний.
     */
    @Override
    @Audit(action = "getAllReadings")
    @Loggable
    public Map<String, List<UserReadingRecord>> getAllReadings() throws MyDatabaseException {
        List<UserReadingRecord> readings = readingRepo.getAll();
        return readings.stream()
                .collect(Collectors.groupingBy(UserReadingRecord::accountNum));
    }
}
