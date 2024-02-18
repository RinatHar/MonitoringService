package org.kharisov.mappers;

import org.kharisov.dtos.UserDto;
import org.kharisov.entities.UserRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс UserMapper предоставляет функциональность преобразования между DTO и сущностью.
 * Этот интерфейс использует библиотеку MapStruct для автоматического маппинга между DTO и сущностью.
 *
 * @see org.mapstruct.Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * Экземпляр UserMapper, созданный с помощью MapStruct.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразует UserDtoIn в сущность User.
     *
     * @param userDTO DTO пользователя для преобразования
     * @return Сущность User, соответствующая переданному DTO
     */
    UserRecord toEntity(UserDto userDTO);

    default UserRecord toEntityWithRole(UserDto userDTO, Long roleId) {
        UserRecord user = toEntity(userDTO);
        return new UserRecord(
                null,
                user.accountNum(),
                user.password(),
                roleId
        );
    }
}
