package org.kharisov.mappers;

import org.kharisov.dtos.ReadingTypeDto;
import org.kharisov.entities.ReadingTypeRecord;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс ReadingTypeMapper предоставляет функциональность преобразования между DTO и сущностью.
 * Этот интерфейс использует библиотеку MapStruct для автоматического маппинга между DTO и сущностью.
 *
 * @see org.mapstruct.Mapper
 */
@Mapper
public interface ReadingTypeMapper {

    /**
     * Экземпляр ReadingTypeMapper, созданный с помощью MapStruct.
     */
    ReadingTypeMapper INSTANCE = Mappers.getMapper(ReadingTypeMapper.class);

    /**
     * Преобразует ReadingTypeDtoIn в сущность ReadingType.
     * Использует преобразование MapStruct для преобразования поля "name" в ReadingTypeDtoIn.
     *
     * @param dto DTO типа показания для преобразования
     * @return Сущность ReadingType, соответствующая переданному DTO
     */
    @Mapping(target = "name", source = "name")
    ReadingTypeRecord toEntity(ReadingTypeDto dto);
}
