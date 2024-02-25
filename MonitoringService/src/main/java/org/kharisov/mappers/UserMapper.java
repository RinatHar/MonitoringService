package org.kharisov.mappers;

import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.dtos.UserDto;
import org.kharisov.enums.Role;
import org.mapstruct.*;
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
    @Mapping(target = "accountNum", source = "accountNum")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    UserRecord toEntity(UserDto userDTO);

    /**
     * Преобразует UserDto в сущность User с заданным идентификатором роли.
     *
     * @param userDTO DTO пользователя для преобразования.
     * @param roleId Идентификатор роли для присвоения пользователю.
     * @return Сущность User, соответствующая переданному DTO и с заданным идентификатором роли.
     */
    default UserRecord toEntityWithRole(UserDto userDTO, Role role) {
        UserRecord user = toEntity(userDTO);
        return new UserRecord(
                null,
                user.accountNum(),
                user.password(),
                role.name()
        );
    }
}
