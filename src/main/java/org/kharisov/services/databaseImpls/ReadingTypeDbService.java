package org.kharisov.services.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.annotations.Audit;
import org.kharisov.entities.ReadingTypeRecord;
import org.kharisov.exceptions.*;
import org.kharisov.repos.interfaces.ReadingTypeRepo;
import org.kharisov.services.interfaces.ReadingTypeService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс ReadingTypeDbService представляет собой службу для работы с типами показаний в базе данных.
 * Он реализует интерфейс ReadingTypeService и использует репозиторий ReadingTypeDbRepo для выполнения операций с базой данных.
 */
@RequiredArgsConstructor
public class ReadingTypeDbService implements ReadingTypeService {
    /**
     * Репозиторий для работы с типами показаний.
     */
    private final ReadingTypeRepo readingTypeRepo;

    /**
     * Добавляет новый тип показания.
     *
     * @param readingTypeRecord Новый тип показания.
     * @return ReadingTypeRecord Добавленный тип показания
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     * @throws ConflictException   Если тип показания с указанным именем уже существует.
     */
    @Override
    @Audit(action = "addReadingType")
    public ReadingTypeRecord addReadingType(ReadingTypeRecord readingTypeRecord) throws MyDatabaseException, ConflictException {
        if (readingTypeRepo.getByName(readingTypeRecord.name()).isPresent())
            throw new ConflictException("The type of reading already exists");
        return readingTypeRepo.add(readingTypeRecord).get();
    }

    /**
     * Возвращает тип показания по его имени.
     *
     * @param name Имя типа показания.
     * @return Объект типа показания.
     * @throws MyDatabaseException Если произошла ошибка при взаимодействии с базой данных.
     * @throws EntityNotFoundException Если тип показания с указанным именем не найден.
     */
    @Override
    public ReadingTypeRecord getByName(String name) throws MyDatabaseException, EntityNotFoundException {
        Optional<ReadingTypeRecord> readingTypeRecordOptional = readingTypeRepo.getByName(name);
        if (readingTypeRecordOptional.isEmpty())
            throw new EntityNotFoundException("The type of reading was not found");
        return readingTypeRecordOptional.get();
    }

    /**
     * Возвращает имена всех типов показаний.
     *
     * @return Набор имен всех типов показаний.
     */
    @Override
    public Set<String> getReadingNames() throws MyDatabaseException {
        List<ReadingTypeRecord> records = readingTypeRepo.getAll();
        return records.stream().map(ReadingTypeRecord::name).collect(Collectors.toSet());
    }
}
