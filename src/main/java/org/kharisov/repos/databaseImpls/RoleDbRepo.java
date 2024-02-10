package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.db.RoleDto;

import java.sql.*;
import java.util.*;

/**
 * Класс RoleDbRepo представляет собой репозиторий для работы с ролями в базе данных.
 * Он наследуется от базового класса репозитория BaseDbRepo и реализует его абстрактные методы.
 */
public class RoleDbRepo extends BaseDbRepo<Long, RoleDto> {

    /**
     * Конструктор класса RoleDbRepo.
     *
     * @param connectionPool Пул соединений с базой данных.
     */
    public RoleDbRepo(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    /**
     * Добавляет роль в базу данных и возвращает ее.
     *
     * @param dto Объект DTO роли для добавления в базу данных.
     * @return Объект DTO роли, добавленной в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<RoleDto> add(RoleDto dto) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO roles (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setId(generatedKeys.getLong(1));
                    return Optional.of(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot add role", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает роль по ее идентификатору.
     *
     * @param id Идентификатор роли, которую требуется получить.
     * @return Объект DTO роли с указанным идентификатором или пустой Optional, если такой роли не существует.
     */
    public Optional<RoleDto> getById(Long id) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM roles where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RoleDto role = new RoleDto();
                    role.setId(resultSet.getLong("id"));
                    role.setName(resultSet.getString("name"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get role by id", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return Optional.empty();
    }

    /**
     * Возвращает роль по ее имени.
     *
     * @param name Имя роли, которую требуется получить.
     * @return Объект DTO роли с указанным именем или пустой Optional, если такой роли не существует.
     */
    public Optional<RoleDto> getByName(String name) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM roles where name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RoleDto role = new RoleDto();
                    role.setId(resultSet.getLong("id"));
                    role.setName(resultSet.getString("name"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get role by name", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return Optional.empty();
    }

    /**
     * Изменяет роль пользователя по номеру счета.
     *
     * @param accountNum Номер счета пользователя, для которого требуется изменить роль.
     * @param roleDto Объект DTO роли, который представляет новую роль, которую нужно установить.
     * @return Объект DTO обновленной роли или пустой Optional, если обновление не удалось.
     */
    public Optional<RoleDto> changeRoleByAccountNum(String accountNum, RoleDto roleDto) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "UPDATE users " +
                "SET role_id = (SELECT id FROM roles WHERE name = ?) " +
                "WHERE account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roleDto.getName());
            statement.setString(2, accountNum);
            int updatedRows = statement.executeUpdate();

            if (updatedRows > 0) {
                return Optional.of(roleDto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot update role", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return Optional.empty();
    }

    /**
     * Возвращает все роли из базы данных.
     *
     * @return Список всех объектов DTO ролей из базы данных.
     */
    @Override
    public List<RoleDto> getAll() {
        Connection connection = connectionPool.getConnectionFromPool();
        List<RoleDto> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RoleDto role = new RoleDto();
                role.setId(resultSet.getLong("id"));
                role.setName(resultSet.getString("name"));
                roles.add(role);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get all roles", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return roles;
    }
}
