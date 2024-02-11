package org.kharisov.mappers;

import org.kharisov.domains.*;
import org.kharisov.dtos.in.ReadingDtoIn;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс ReadingMapper предоставляет функциональность преобразования между DTO и сущностью.
 * Этот интерфейс использует библиотеку MapStruct для автоматического маппинга между DTO и сущностью.
 *
 * @see org.mapstruct.Mapper
 */
@Mapper
public interface ReadingMapper {

    /**
     * Экземпляр ReadingMapper, созданный с помощью MapStruct.
     */
    ReadingMapper INSTANCE = Mappers.getMapper(ReadingMapper.class);

    /**
     * Преобразует ReadingDtoIn в сущность ReadingRecord.
     * Использует именованное преобразование "stringToReadingType" для преобразования строки в ReadingType.
     *
     * @param dto DTO показания для преобразования
     * @return Сущность ReadingRecord, соответствующая переданному DTO
     */
    @Mapping(source = "type", target = "type", qualifiedByName = "stringToReadingType")
    ReadingRecord toEntity(ReadingDtoIn dto);

    /**
     * Преобразует строку в ReadingType.
     * Используется для преобразования поля "type" в ReadingDtoIn.
     *
     * @param typeString строка для преобразования
     * @return ReadingType, соответствующий переданной строке
     */
    @Named("stringToReadingType")
    default ReadingType stringToReadingType(String typeString) {
        return ReadingType.Create(typeString);
    }
}

