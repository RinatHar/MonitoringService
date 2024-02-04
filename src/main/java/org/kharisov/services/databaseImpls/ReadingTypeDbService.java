package org.kharisov.services.databaseImpls;

import org.kharisov.dtos.ReadingTypeDto;
import org.kharisov.entities.ReadingType;
import org.kharisov.repos.databaseImpls.ReadingTypeDbRepo;
import org.kharisov.services.interfaces.ReadingTypeService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс ReadingTypeDbService представляет собой службу для работы с типами показаний в базе данных.
 * Он реализует интерфейс ReadingTypeService и использует репозиторий ReadingTypeDbRepo для выполнения операций с базой данных.
 */
public class ReadingTypeDbService implements ReadingTypeService {
    /**
     * Репозиторий для работы с типами показаний.
     */
    private final ReadingTypeDbRepo readingTypeDbRepo;

    /**
     * Конструктор класса ReadingTypeDbService.
     *
     * @param readingTypeDbRepo Репозиторий для работы с типами показаний.
     */
    public ReadingTypeDbService(ReadingTypeDbRepo readingTypeDbRepo) {
        this.readingTypeDbRepo = readingTypeDbRepo;
    }

    /**
     * Добавляет новый тип показания.
     *
     * @param name Имя нового типа показания.
     */
    @Override
    public void addReadingType(String name) {
        ReadingTypeDto readingTypeDto = new ReadingTypeDto();
        readingTypeDto.setName(name);
        readingTypeDbRepo.add(readingTypeDto);
    }

    /**
     * Возвращает тип показания по его имени.
     *
     * @param name Имя типа показания.
     * @return Объект типа показания или пустой Optional, если тип показания не найден.
     */
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

    /**
     * Возвращает имена всех типов показаний.
     *
     * @return Набор имен всех типов показаний.
     */
    @Override
    public Set<String> getReadingNames() {
        List<ReadingTypeDto> readingTypes = readingTypeDbRepo.getAll();
        return readingTypes.stream().map(ReadingTypeDto::getName).collect(Collectors.toSet());
    }
}
