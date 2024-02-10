package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.db.ReadingDto;
import org.kharisov.entities.ReadingType;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Класс ReadingDbRepo представляет собой репозиторий для работы с показаниями в базе данных.
 * Он наследуется от базового класса репозитория BaseDbRepo и реализует его абстрактные методы.
 */
public class ReadingDbRepo extends BaseDbRepo<Long, ReadingDto> {

    /**
     * Конструктор класса ReadingDbRepo.
     *
     * @param connectionPool Пул соединений с базой данных.
     */
    public ReadingDbRepo(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    /**
     * Добавляет показание в базу данных и возвращает его.
     *
     * @param dto Объект DTO показания для добавления в базу данных.
     * @return Объект DTO показания, добавленного в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<ReadingDto> add(ReadingDto dto) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO readings (type_id, user_id, value, date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, dto.getTypeId());
            statement.setLong(2, dto.getUserId());
            statement.setInt(3, dto.getValue());
            statement.setDate(4, Date.valueOf(dto.getDate()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setId(generatedKeys.getLong(1));
                    return Optional.of(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot add reading", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает все показания для указанного номера счета.
     *
     * @param accountNum Номер счета, для которого требуется получить показания.
     * @return Список объектов DTO показаний для указанного номера счета.
     */
    public List<ReadingDto> getAllByAccountNum(String accountNum) {
        Connection connection = connectionPool.getConnectionFromPool();
        List<ReadingDto> readings = new ArrayList<>();
        String sql = "SELECT readings.id, type_id, t.name, user_id, account_num, value, date FROM readings\n" +
                "    left join users u on u.id = readings.user_id " +
                "    left join reading_types t on t.id = readings.type_id where u.account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ReadingDto reading = new ReadingDto();
                    reading.setId(resultSet.getLong("id"));
                    reading.setUserId(resultSet.getLong("user_id"));
                    reading.setAccountNum(resultSet.getString("account_num"));
                    reading.setTypeId(resultSet.getLong("type_id"));
                    reading.setType(ReadingType.Create(resultSet.getString("name")));
                    reading.setValue(resultSet.getInt("value"));
                    reading.setDate(resultSet.getDate("date").toLocalDate());
                    readings.add(reading);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get readings by accountNum", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return readings;
    }

    /**
     * Возвращает все показания из базы данных.
     *
     * @return Список всех объектов DTO показаний из базы данных.
     */
    @Override
    public List<ReadingDto> getAll() {
        Connection connection = connectionPool.getConnectionFromPool();
        List<ReadingDto> readings = new ArrayList<>();
        String sql = "SELECT readings.id, type_id, u.account_num, name, value, date FROM readings " +
                "left join reading_types t on t.id = readings.type_id " +
                "left join users u on u.id = readings.user_id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ReadingDto reading = new ReadingDto();
                reading.setId(resultSet.getLong("id"));
                reading.setTypeId(resultSet.getLong("type_id"));
                reading.setType(ReadingType.Create(resultSet.getString("name")));
                reading.setAccountNum(resultSet.getString("account_num"));
                reading.setValue(resultSet.getInt("value"));
                reading.setDate(resultSet.getDate("date").toLocalDate());
                readings.add(reading);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get all readings", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return readings;
    }
}
