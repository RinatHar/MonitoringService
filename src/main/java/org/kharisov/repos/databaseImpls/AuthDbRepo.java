package org.kharisov.repos.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.configs.ConnectionPool;
import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.repos.interfaces.*;

import java.sql.*;
import java.util.*;

/**
 * Класс UserDbRepo представляет собой реализацию интерфейса UserRepo.
 * Он предоставляет методы для работы с пользователями в базе данных.
 */
@RequiredArgsConstructor
public class AuthDbRepo implements AuthRepo {

    /**
     * Пул соединений с базой данных, используемый в репозитории.
     */
    private final ConnectionPool connectionPool;

    /**
     * Добавляет пользователя в базу данных и возвращает его.
     *
     * @param record Объект сущности пользователя для добавления в базу данных.
     * @return Объект сущности пользователя, добавленного в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<UserRecord> addUser(UserRecord record) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO users (account_num, password, role_id) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, record.accountNum());
            statement.setString(2, record.password());
            statement.setLong(3, record.role_id());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record = new UserRecord(
                            generatedKeys.getLong(1),
                            record.accountNum(),
                            record.password(),
                            record.role_id()
                    );
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось добавить пользователя в БД" + record, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает пользователя по его номеру счета.
     *
     * @param accountNum Номер счета пользователя, которого требуется получить.
     * @return Объект сущности пользователя с указанным номером счета или пустой Optional, если такого пользователя не существует.
     */
    @Override
    public Optional<UserRecord> getUserByAccountNum(String accountNum) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM users where account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserRecord record = new UserRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("account_num"),
                            resultSet.getString("password"),
                            resultSet.getLong("role_id")
                    );
                    return Optional.of(record);
                }
            }

        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать пользователя из БД", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя, которого требуется получить.
     * @return Объект сущности пользователя с указанным номером счета или пустой Optional, если такого пользователя не существует.
     */
    @Override
    public Optional<UserRecord> getUserById(Long id) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM users where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserRecord record = new UserRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("account_num"),
                            resultSet.getString("password"),
                            resultSet.getLong("role_id")
                    );
                    return Optional.of(record);
                }
            }

        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать все возможные роли из БД", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает всех пользователей из базы данных.
     *
     * @return Список всех объектов сущности пользователей из базы данных.
     */
    @Override
    public List<UserRecord> getAllUsers() throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<UserRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserRecord record = new UserRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("account_num"),
                        resultSet.getString("password"),
                        resultSet.getLong("role_id")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать всех пользователей из БД", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return records;
    }

    /**
     * Добавляет роль в базу данных и возвращает ее.
     *
     * @param record Объект сущности роли для добавления в базу данных.
     * @return Объект сущности роли, добавленной в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<RoleRecord> addRole(RoleRecord record) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO roles (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, record.name());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record = new RoleRecord(
                            generatedKeys.getLong(1),
                            record.name()
                    );
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось добавить новую роль " + record, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает роль по ее идентификатору.
     *
     * @param id Идентификатор роли, которую требуется получить.
     * @return Объект сущности роли с указанным идентификатором или пустой Optional, если такой роли не существует.
     */
    @Override
    public Optional<RoleRecord> getRoleById(Long id) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM roles where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RoleRecord record = new RoleRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    );
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось получить роль по id " + id, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return Optional.empty();
    }

    /**
     * Возвращает роль по ее имени.
     *
     * @param role Роль, которую требуется получить.
     * @return Объект сущности роли с указанным именем или пустой Optional, если такой роли не существует.
     */
    @Override
    public Optional<RoleRecord> getRoleByName(Role role) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM roles where name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RoleRecord record = new RoleRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    );
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать роль по названию " + role.name(), e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return Optional.empty();
    }

    /**
     * Изменяет роль пользователя по номеру счета.
     *
     * @param accountNum Номер счета пользователя, для которого требуется изменить роль.
     * @param record Объект сущности роли, представляет новую роль, которую нужно установить.
     * @return Объект сущности обновленной роли или пустой Optional, если обновление не удалось.
     */
    @Override
    public Optional<RoleRecord> changeRoleByAccountNum(String accountNum, RoleRecord record) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "UPDATE users " +
                "SET role_id = (SELECT id FROM roles WHERE name = ?) " +
                "WHERE account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, record.name());
            statement.setString(2, accountNum);
            int updatedRows = statement.executeUpdate();

            if (updatedRows > 0) {
                return Optional.of(record);
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось изменить роль пользователя с номером счета " + accountNum +
                    " на роль " + record, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return Optional.empty();
    }

    @Override
    public List<RoleRecord> getAllRoles() throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<RoleRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM roles";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RoleRecord record = new RoleRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать все возможные роли", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return records;
    }
}
