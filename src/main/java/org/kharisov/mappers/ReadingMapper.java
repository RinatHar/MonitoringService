package org.kharisov.mappers;

import org.kharisov.dtos.ReadingDto;
import org.kharisov.entities.*;
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
     * Преобразует UserReadingRecord в ReadingDto.
     *
     * @param record Сущность показания для преобразования
     * @return ReadingDto, соответствующая переданной сущности
     */
    @Mapping(target = "type", source = "type")
    @Mapping(target = "value", source = "value")
    ReadingDto toDto(UserReadingRecord record);

    /**
     * Преобразует ReadingDtoIn в сущность ReadingRecord.
     *
     * @param dto DTO показания для преобразования
     * @return Сущность ReadingRecord, соответствующая переданному DTO
     */
    @Mapping(target = "value", source = "value")
    ReadingRecord toEntity(ReadingDto dto);

}

