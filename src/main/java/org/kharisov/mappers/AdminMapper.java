package org.kharisov.mappers;

import org.kharisov.dtos.in.AdminDtoIn;
import org.kharisov.dtos.in.UserDtoIn;
import org.kharisov.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    User toEntity(AdminDtoIn adminDtoIn);
}
