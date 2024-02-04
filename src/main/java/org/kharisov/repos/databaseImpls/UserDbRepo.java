package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.UserDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbRepo extends BaseDbRepo<Long, UserDto> {

    @Override
    public Optional<UserDto> add(UserDto dto) {
        String sql = "INSERT INTO users (account_num, password, role_id) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getAccountNum());
            statement.setString(2, dto.getPassword());
            statement.setLong(3, dto.getRoleId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setId(generatedKeys.getLong(1));
                    return Optional.of(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot add user", e);
        }

        return Optional.empty();
    }

    public Optional<UserDto> getByAccountNum(String accountNum) {
        String sql = "SELECT * FROM users where account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserDto user = new UserDto();
                    user.setId(resultSet.getLong("id"));
                    user.setAccountNum(resultSet.getString("account_num"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRoleId(resultSet.getLong("role_id"));
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Cannot get user", e);
        }
        return Optional.empty();
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserDto user = new UserDto();
                user.setId(resultSet.getLong("id"));
                user.setAccountNum(resultSet.getString("account_num"));
                user.setPassword(resultSet.getString("password"));
                user.setRoleId(resultSet.getLong("role_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get all users", e);
        }

        return users;
    }
}
