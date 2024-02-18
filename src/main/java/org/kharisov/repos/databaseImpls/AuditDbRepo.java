package org.kharisov.repos.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.configs.ConnectionPool;
import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.repos.interfaces.AuditRepo;

import java.sql.*;
import java.util.*;

/**
 * Класс AuditDbRepo представляет собой реализацию интерфейса AuditRepo.
 * Он предоставляет методы для работы с записями аудита в базе данных.
 */
@RequiredArgsConstructor
public class AuditDbRepo implements AuditRepo {

    /**
     * Пул соединений с базой данных, используемый в репозитории.
     */
    private final ConnectionPool connectionPool;

    /**
     * Добавляет запись аудита в базу данных и возвращает ее.
     *
     * @param record Объект сущности записи аудита для добавления в базу данных.
     * @return Объект сущности записи аудита, добавленной в базу данных, или пустой Optional, если добавление не удалось.
     */
    public Optional<AuditRecord> add(AuditRecord record) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO audit (action, user_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, record.action());
            statement.setLong(2, record.userId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record = new AuditRecord(
                            generatedKeys.getLong(1),
                            record.action(),
                            record.userId());
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось добавить аудит " + record, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает все записи аудита для указанного номера счета.
     *
     * @param accountNum Номер счета, для которого требуется получить записи аудита.
     * @return Список объектов сущностей записей аудита с указанным номером счета.
     */
    public List<UserAuditRecord> getAuditRecordsByAccountNum(String accountNum) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<UserAuditRecord> entries = new ArrayList<>();
        String sql = "SELECT audit.id, action, u.account_num FROM audit " +
                "join users u on u.id = audit.user_id where u.account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UserAuditRecord record = new UserAuditRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("action"),
                            resultSet.getString("account_num")
                    );
                    entries.add(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать аудит по номеру счета " + accountNum, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return entries;
    }


    /**
     * Возвращает все записи аудита из базы данных.
     *
     * @return Список всех объектов сущностей записей аудита из базы данных.
     */
    public List<UserAuditRecord> getAll() throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<UserAuditRecord> records = new ArrayList<>();
        String sql = "SELECT audit.id, action, u.account_num FROM audit\n" +
                "    join users u on u.id = audit.user_id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserAuditRecord record = new UserAuditRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("action"),
                        resultSet.getString("account_num")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать весь аудит", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return records;
    }
}

