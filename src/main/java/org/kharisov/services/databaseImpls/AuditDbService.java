package org.kharisov.services.databaseImpls;

import org.kharisov.dtos.db.EntryDto;
import org.kharisov.dtos.db.UserDto;
import org.kharisov.entities.User;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.interfaces.AuditService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс AuditDbService представляет собой сервис для работы с аудитом в базе данных.
 */
public class AuditDbService implements AuditService {
    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserDbRepo userDbRepo;
    /**
     * Репозиторий для работы с аудитом.
     */
    private final AuditDbRepo auditDbRepo;

    /**
     * Конструктор класса AuditDbService.
     *
     * @param userDbRepo  Репозиторий для работы с пользователями.
     * @param auditDbRepo Репозиторий для работы с аудитом.
     */
    public AuditDbService(UserDbRepo userDbRepo, AuditDbRepo auditDbRepo) {
        this.userDbRepo = userDbRepo;
        this.auditDbRepo = auditDbRepo;
    }

    /**
     * Добавляет запись аудита для указанного пользователя и действия.
     *
     * @param user   Пользователь, для которого добавляется запись аудита.
     * @param action Действие, которое должно быть записано в аудите.
     */
    @Override
    public void addEntry(User user, String action) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(user.getAccountNum());
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            Long userId = userDto.getId();

            EntryDto entryDto = new EntryDto();
            entryDto.setUserId(userId);
            entryDto.setAction(action);

            auditDbRepo.add(entryDto);
        }
    }

    /**
     * Возвращает все записи аудита для указанного пользователя.
     *
     * @param user Пользователь, для которого требуется получить записи аудита.
     * @return Список действий из записей аудита для указанного пользователя.
     */
    @Override
    public List<String> getEntries(User user) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(user.getAccountNum());
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            List<EntryDto> entries = auditDbRepo.getEntriesByAccountNum(userDto.getAccountNum());
            return entries.stream().map(EntryDto::getAction).collect(Collectors.toList());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * Возвращает все записи аудита из базы данных.
     *
     * @return Map, где ключ - это номер счета, а значение - это список действий из записей аудита.
     */
    @Override
    public Map<String, List<String>> getAllEntries() {
        List<EntryDto> entries = auditDbRepo.getAll();
        return entries.stream().collect(Collectors.groupingBy(EntryDto::getAccountNum, Collectors.mapping(EntryDto::getAction, Collectors.toList())));
    }
}
