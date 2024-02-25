package org.kharisov.repos.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.configs.ConnectionPool;
import org.kharisov.entities.*;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.repos.interfaces.ReadingRepo;

import java.sql.Date;
import java.sql.*;
import java.util.*;

/**
 * Класс ReadingDbRepo представляет собой реализацию интерфейса ReadingRepo.
 * Он предоставляет методы для работы с показаниями в базе данных.
 */
@RequiredArgsConstructor
public class ReadingDbRepo implements ReadingRepo {

    /**
     * Пул соединений с базой данных, используемый в репозитории.
     */
    private final ConnectionPool connectionPool;

    /**
     * Добавляет показание в базу данных и возвращает его.
     *
     * @param record Объект сущности показания для добавления в базу данных.
     * @return Объект сущности показания, добавленного в базу данных, или пустой Optional, если добавление не удалось.
     */
    public Optional<ReadingRecord> add(ReadingRecord record) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO readings (type_id, user_id, value, date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, record.typeId());
            statement.setLong(2, record.userId());
            statement.setInt(3, record.value());
            statement.setDate(4, Date.valueOf(record.date()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record = new ReadingRecord(
                            generatedKeys.getLong(1),
                            record.userId(),
                            record.typeId(),
                            record.value(),
                            record.date()
                    );
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось добавить новое показание " + record, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает все показания для указанного номера счета.
     *
     * @param accountNum Номер счета, для которого требуется получить показания.
     * @return Список объектов сущности показаний для указанного номера счета.
     */
    public List<UserReadingRecord> getAllByAccountNum(String accountNum) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<UserReadingRecord> records = new ArrayList<>();
        String sql = "SELECT readings.id, rt.name, u.account_num, value, date FROM readings " +
                "join users u on u.id = readings.user_id " +
                "join reading_types rt on rt.id = readings.type_id " +
                "where u.account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UserReadingRecord record = new UserReadingRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("account_num"),
                            resultSet.getString("name"),
                            resultSet.getInt("value"),
                            resultSet.getDate("date").toLocalDate()
                    );
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать все показания по номеру счета " + accountNum, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return records;
    }

    /**
     * Возвращает все показания из базы данных.
     *
     * @return Список всех объектов сущности показаний из базы данных.
     */
    public List<UserReadingRecord> getAll() throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<UserReadingRecord> records = new ArrayList<>();
        String sql = "SELECT readings.id, rt.name, u.account_num, value, date FROM readings " +
                "join users u on u.id = readings.user_id " +
                "join reading_types rt on rt.id = readings.type_id ";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserReadingRecord record = new UserReadingRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("account_num"),
                        resultSet.getString("name"),
                        resultSet.getInt("value"),
                        resultSet.getDate("date").toLocalDate()
                );
                records.add(record);
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать все показания", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return records;
    }
}
