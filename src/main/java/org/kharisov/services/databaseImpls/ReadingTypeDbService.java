package org.kharisov.services.databaseImpls;

import org.kharisov.dtos.ReadingTypeDto;
import org.kharisov.entities.ReadingType;
import org.kharisov.repos.databaseImpls.ReadingTypeDbRepo;
import org.kharisov.services.interfaces.ReadingTypeService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReadingTypeDbService implements ReadingTypeService {
    private final ReadingTypeDbRepo readingTypeDbRepo;

    public ReadingTypeDbService(ReadingTypeDbRepo readingTypeDbRepo) {
        this.readingTypeDbRepo = readingTypeDbRepo;
    }

    @Override
    public void addReadingType(String name) {
        ReadingTypeDto readingTypeDto = new ReadingTypeDto();
        readingTypeDto.setName(name);
        readingTypeDbRepo.add(readingTypeDto);
    }

    @Override
    public Optional<ReadingType> getReadingType(String name) {
        Optional<ReadingTypeDto> readingTypeDtoOptional = readingTypeDbRepo.getByName(name);
        if (readingTypeDtoOptional.isPresent()) {
            ReadingTypeDto readingTypeDto = readingTypeDtoOptional.get();
            return Optional.of(ReadingType.Create(readingTypeDto.getName()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<String> getReadingNames() {
        List<ReadingTypeDto> readingTypes = readingTypeDbRepo.getAll();
        return readingTypes.stream().map(ReadingTypeDto::getName).collect(Collectors.toSet());
    }
}
