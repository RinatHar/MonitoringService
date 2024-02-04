package org.kharisov.services.databaseImpls;

import org.kharisov.dtos.ReadingDto;
import org.kharisov.dtos.ReadingTypeDto;
import org.kharisov.dtos.UserDto;
import org.kharisov.entities.ReadingRecord;
import org.kharisov.entities.ReadingType;
import org.kharisov.entities.User;
import org.kharisov.repos.databaseImpls.ReadingDbRepo;
import org.kharisov.repos.databaseImpls.ReadingTypeDbRepo;
import org.kharisov.repos.databaseImpls.UserDbRepo;
import org.kharisov.services.interfaces.ReadingService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReadingDbService implements ReadingService {
    private final ReadingDbRepo readingDbRepo;
    private final UserDbRepo userDbRepo;
    private final ReadingTypeDbRepo readingTypeDbRepo;

    public ReadingDbService(ReadingDbRepo readingDbRepo,
                            UserDbRepo userDbRepo,
                            ReadingTypeDbRepo readingTypeDbRepo) {
        this.readingDbRepo = readingDbRepo;
        this.userDbRepo = userDbRepo;
        this.readingTypeDbRepo = readingTypeDbRepo;
    }

    @Override
    public void addReading(User user, ReadingType reading, int value) {
        ReadingDto readingDto = new ReadingDto();
        Optional<UserDto> optionalUserDto = userDbRepo.getByAccountNum(user.getAccountNum());
        Optional<ReadingTypeDto> optionalReadingTypeDto = readingTypeDbRepo.getByName(reading.getValue());
        if (optionalUserDto.isPresent() && optionalReadingTypeDto.isPresent()) {
            UserDto userDto = optionalUserDto.get();
            ReadingTypeDto readingTypeDto = optionalReadingTypeDto.get();

            readingDto.setUserId(userDto.getId());
            readingDto.setTypeId(readingTypeDto.getId());
            readingDto.setValue(value);
            readingDto.setDate(LocalDate.now());

            readingDbRepo.add(readingDto);
        }

    }

    @Override
    public boolean readingExists(User user, ReadingType reading, LocalDate now) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream().anyMatch(r -> r.getType().equals(reading) &&
                r.getDate().getYear() == now.getYear() &&
                r.getDate().getMonth() == now.getMonth());
    }

    @Override
    public List<ReadingRecord> getReadingsByMonth(User user, int month, int year) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream()
                .filter(r -> r.getDate().getMonthValue() == month && r.getDate().getYear() == year)
                .map(r -> ReadingRecord.builder()
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReadingRecord> getCurrentReading(User user, ReadingType type) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream()
                .filter(r -> r.getType().equals(type))
                .max(Comparator.comparing(ReadingDto::getDate))
                .map(r -> ReadingRecord.builder()
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build());
    }

    @Override
    public List<ReadingRecord> getHistory(User user) {
        List<ReadingDto> readings = readingDbRepo.getAllByAccountNum(user.getAccountNum());
        return readings.stream()
                .map(r -> ReadingRecord.builder()
                        .type(r.getType())
                        .value(r.getValue())
                        .date(r.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
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
