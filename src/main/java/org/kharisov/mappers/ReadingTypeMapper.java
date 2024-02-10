package org.kharisov.mappers;

import org.kharisov.dtos.in.ReadingTypeDtoIn;
import org.kharisov.entities.ReadingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReadingTypeMapper {
    ReadingTypeMapper INSTANCE = Mappers.getMapper(ReadingTypeMapper.class);

    @Mapping(target = "name", source = "name")
    ReadingType toEntity(ReadingTypeDtoIn dto);
}
