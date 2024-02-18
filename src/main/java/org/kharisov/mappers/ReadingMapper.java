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
     * Преобразует ReadingDtoIn в сущность ReadingRecord.
     *
     * @param dto DTO показания для преобразования
     * @return Сущность ReadingRecord, соответствующая переданному DTO
     */
    ReadingRecord toEntity(ReadingDto dto);

}

