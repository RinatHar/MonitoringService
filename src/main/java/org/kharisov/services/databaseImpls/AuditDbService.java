package org.kharisov.services.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.repos.interfaces.*;
import org.kharisov.services.interfaces.AuditService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс AuditDbService представляет собой сервис для работы с аудитом в базе данных.
 */
@RequiredArgsConstructor
public class AuditDbService implements AuditService {
    /**
     * Репозиторий для работы с пользователями.
     */
    private final AuthRepo authRepo;
    /**
     * Репозиторий для работы с аудитом.
     */
    private final AuditRepo auditRepo;

    /**
     * Добавляет запись аудита для указанного пользователя и действия.
     *
     * @param user   Пользователь, для которого добавляется запись аудита.
     * @param action Действие, которое должно быть записано в аудите.
     */
    @Override
    public void addAuditRecord(UserRecord user, String action) throws MyDatabaseException {
        AuditRecord record = new AuditRecord(
                null,
                action,
                user.id()
        );
        auditRepo.add(record);
    }

    /**
     * Возвращает все записи аудита для указанного пользователя.
     *
     * @param user Пользователь, для которого требуется получить записи аудита.
     * @return Список действий из записей аудита для указанного пользователя.
     */
    @Override
    public List<String> getAuditRecords(UserRecord user) throws MyDatabaseException {
        Optional<UserRecord> userRecordOptional = authRepo.getUserByAccountNum(user.accountNum());
        if (userRecordOptional.isPresent()) {
            List<UserAuditRecord> records = auditRepo.getAuditRecordsByAccountNum(user.accountNum());
            return records.stream().map(UserAuditRecord::action).collect(Collectors.toList());
        } else {
            throw new RuntimeException("The user was not found");
        }
    }

    /**
     * Возвращает все записи аудита из базы данных.
     *
     * @return Map, где ключ - это номер счета, а значение - это список действий из записей аудита.
     */
    @Override
    public Map<String, List<String>> getAllAuditRecords() throws MyDatabaseException {
        List<UserAuditRecord> records = auditRepo.getAll();
        return records.stream().collect(Collectors.groupingBy(UserAuditRecord::accountNum, Collectors.mapping(UserAuditRecord::action, Collectors.toList())));
    }
}
