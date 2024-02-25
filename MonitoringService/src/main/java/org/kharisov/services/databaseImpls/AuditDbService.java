package org.kharisov.services.databaseImpls;

import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.auditshared.services.interfaces.AuditService;
import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.repos.interfaces.AuditRepo;
import org.kharisov.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс AuditDbService представляет собой сервис для работы с аудитом в базе данных.
 */
@Service
public class AuditDbService implements AuditService {

    /**
     * Сервис для работы с пользователями.
     */
    private final AuthService authService;

    /**
     * Репозиторий для работы с аудитом.
     */
    private final AuditRepo auditRepo;

    @Autowired
    public AuditDbService(AuthService authService, AuditRepo auditRepo) {
        this.authService = authService;
        this.auditRepo = auditRepo;
    }

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
        authService.getUserByAccountNum(user.accountNum());
        List<UserAuditRecord> records = auditRepo.getAuditRecordsByAccountNum(user.accountNum());
        return records.stream().map(UserAuditRecord::action).collect(Collectors.toList());
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
