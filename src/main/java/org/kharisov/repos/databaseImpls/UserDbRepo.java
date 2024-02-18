package org.kharisov.repos.databaseImpls;

import org.kharisov.configs.ConnectionPool;
import org.kharisov.dtos.db.UserDto;
import org.kharisov.repos.base.BaseDbRepo;

import java.sql.*;
import java.util.*;

/**
 * Класс UserDbRepo представляет собой репозиторий для работы с пользователями в базе данных.
 * Он наследуется от базового класса репозитория BaseDbRepo и реализует его абстрактные методы.
 */
public class UserDbRepo extends BaseDbRepo<Long, UserDto> {

    /**
     * Конструктор класса UserDbRepo.
     *
     * @param connectionPool Пул соединений с базой данных.
     */
    public UserDbRepo(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    /**
     * Добавляет пользователя в базу данных и возвращает его.
     *
     * @param dto Объект DTO пользователя для добавления в базу данных.
     * @return Объект DTO пользователя, добавленного в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<UserDto> add(UserDto dto) {
        Connection connection = connectionPool.getConnectionFromPool();
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
        } catch (SQLException ignored) {

        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает пользователя по его номеру счета.
     *
     * @param accountNum Номер счета пользователя, которого требуется получить.
     * @return Объект DTO пользователя с указанным номером счета или пустой Optional, если такого пользователя не существует.
     */
    public Optional<UserDto> getByAccountNum(String accountNum) {
        Connection connection = connectionPool.getConnectionFromPool();
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

        } catch (SQLException ignored) {

        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает всех пользователей из базы данных.
     *
     * @return Список всех объектов DTO пользователей из базы данных.
     */
    @Override
    public List<UserDto> getAll() {
        Connection connection = connectionPool.getConnectionFromPool();
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
        } catch (SQLException ignored) {

        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return users;
    }
}
