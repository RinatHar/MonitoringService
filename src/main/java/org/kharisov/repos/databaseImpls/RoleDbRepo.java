package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.RoleDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleDbRepo extends BaseDbRepo<Long, RoleDto> {

    @Override
    public Optional<RoleDto> add(RoleDto dto) {
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
        }
        return Optional.empty();
    }

    public Optional<RoleDto> getById(Long id) {
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
            throw new RuntimeException("Cannot get role by namer", e);
        }

        return Optional.empty();
    }

    public Optional<RoleDto> getByName(String name) {
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
        }

        return Optional.empty();
    }

    @Override
    public List<RoleDto> getAll() {
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
        }

        return roles;
    }
}
