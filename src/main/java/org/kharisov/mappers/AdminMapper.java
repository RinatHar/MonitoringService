package org.kharisov.mappers;

import org.kharisov.domains.User;
import org.kharisov.dtos.in.AdminDtoIn;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс AdminMapper предоставляет функциональность преобразования между DTO и сущностью.
 * Этот интерфейс использует библиотеку MapStruct для автоматического маппинга между DTO и сущностью.
 *
 * @see org.mapstruct.Mapper
 */
@Mapper
public interface AdminMapper {

    /**
     * Экземпляр AdminMapper, созданный с помощью MapStruct.
     */
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    /**
     * Преобразует AdminDtoIn в сущность User.
     *
     * @param adminDtoIn DTO администратора для преобразования
     * @return Сущность User, соответствующая переданному DTO
     */
    User toEntity(AdminDtoIn adminDtoIn);
}