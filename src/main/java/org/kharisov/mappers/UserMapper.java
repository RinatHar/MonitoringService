package org.kharisov.mappers;


import org.kharisov.dtos.in.UserDtoIn;
import org.kharisov.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDtoIn userDTO);

    UserDtoIn toDTO(User user);
}
