package org.kharisov.mappers;

import org.kharisov.dtos.in.ReadingDtoIn;
import org.kharisov.entities.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReadingMapper {
    ReadingMapper INSTANCE = Mappers.getMapper(ReadingMapper.class);

    @Mapping(source = "type", target = "type", qualifiedByName = "stringToReadingType")
    ReadingRecord toEntity(ReadingDtoIn dto);

    @Named("stringToReadingType")
    default ReadingType stringToReadingType(String typeString) {
        return ReadingType.Create(typeString);
    }
}
