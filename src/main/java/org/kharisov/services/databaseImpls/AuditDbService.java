package org.kharisov.services.databaseImpls;

import org.kharisov.dtos.EntryDto;
import org.kharisov.dtos.UserDto;
import org.kharisov.entities.User;
import org.kharisov.repos.databaseImpls.AuditDbRepo;
import org.kharisov.repos.databaseImpls.UserDbRepo;
import org.kharisov.services.interfaces.AuditService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuditDbService implements AuditService {
    private final UserDbRepo userDbRepo;
    private final AuditDbRepo auditDbRepo;

    public AuditDbService(UserDbRepo userDbRepo, AuditDbRepo auditDbRepo) {
        this.userDbRepo = userDbRepo;
        this.auditDbRepo = auditDbRepo;
    }
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

    @Override
    public Map<String, List<String>> getAllEntries() {
        List<EntryDto> entries = auditDbRepo.getAll();
        return entries.stream().collect(Collectors.groupingBy(EntryDto::getAccountNum, Collectors.mapping(EntryDto::getAction, Collectors.toList())));
    }
}
