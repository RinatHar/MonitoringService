package org.kharisov.repos.databaseImpls;

import lombok.RequiredArgsConstructor;
import org.kharisov.configs.ConnectionPool;
import org.kharisov.entities.ReadingTypeRecord;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.repos.interfaces.ReadingTypeRepo;

import java.sql.*;
import java.util.*;

/**
 * Класс ReadingTypeRepo представляет собой реализацию интерфейса ReadingTypeRepo.
 * Он предоставляет методы для работы с типами показаний в базе данных.
 */
@RequiredArgsConstructor
public class ReadingTypeDbRepo implements ReadingTypeRepo {

    /**
     * Пул соединений с базой данных, используемый в репозитории.
     */
    private final ConnectionPool connectionPool;

    /**
     * Добавляет тип чтения в базу данных и возвращает его.
     *
     * @param record Объект сущности типа показания для добавления в базу данных.
     * @return Объект сущности типа показания, добавленного в базу данных, или пустой Optional, если добавление не удалось.
     */
    public Optional<ReadingTypeRecord> add(ReadingTypeRecord record) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO reading_types (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, record.name());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record = new ReadingTypeRecord(
                            generatedKeys.getLong(1),
                            record.name()
                    );
                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось добавить новый тип показания " + record, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает тип показания по его имени.
     *
     * @param name Имя типа показания, который требуется получить.
     * @return Объект сущности типа показания с указанным именем или пустой Optional, если такого типа показания не существует.
     */
    public Optional<ReadingTypeRecord> getByName(String name) throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM reading_types where name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ReadingTypeRecord record = new ReadingTypeRecord(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    );
                    return Optional.of(record);
                }
            }

        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать тип показания по названию " + name, e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает все типы показаний из базы данных.
     *
     * @return Список всех объектов сущности типов показаний из базы данных.
     */
    public List<ReadingTypeRecord> getAll() throws MyDatabaseException {
        Connection connection = connectionPool.getConnectionFromPool();
        List<ReadingTypeRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM reading_types";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ReadingTypeRecord record = new ReadingTypeRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            throw new MyDatabaseException("Не получилось достать все типы показаний", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return records;
    }
}
