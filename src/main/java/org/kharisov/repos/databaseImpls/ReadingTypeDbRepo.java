package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.db.ReadingTypeDto;

import java.sql.*;
import java.util.*;

/**
 * Класс ReadingTypeDbRepo представляет собой репозиторий для работы с типами показаний в базе данных.
 * Он наследуется от базового класса репозитория BaseDbRepo и реализует его абстрактные методы.
 */
public class ReadingTypeDbRepo extends BaseDbRepo<Long, ReadingTypeDto> {

    /**
     * Конструктор класса ReadingTypeDbRepo.
     *
     * @param connectionPool Пул соединений с базой данных.
     */
    public ReadingTypeDbRepo(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    /**
     * Добавляет тип чтения в базу данных и возвращает его.
     *
     * @param dto Объект DTO типа показания для добавления в базу данных.
     * @return Объект DTO типа показания, добавленного в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<ReadingTypeDto> add(ReadingTypeDto dto) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO reading_types (name) VALUES (?)";

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
            throw new RuntimeException("Cannot add type", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает тип показания по его имени.
     *
     * @param name Имя типа показания, который требуется получить.
     * @return Объект DTO типа показания с указанным именем или пустой Optional, если такого типа показания не существует.
     */
    public Optional<ReadingTypeDto> getByName(String name) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "SELECT * FROM reading_types where name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ReadingTypeDto type = new ReadingTypeDto();
                    type.setId(resultSet.getLong("id"));
                    type.setName(resultSet.getString("name"));
                    return Optional.of(type);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Cannot get type", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает все типы показаний из базы данных.
     *
     * @return Список всех объектов DTO типов показаний из базы данных.
     */
    @Override
    public List<ReadingTypeDto> getAll() {
        Connection connection = connectionPool.getConnectionFromPool();
        List<ReadingTypeDto> types = new ArrayList<>();
        String sql = "SELECT * FROM reading_types";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ReadingTypeDto type = new ReadingTypeDto();
                type.setId(resultSet.getLong("id"));
                type.setName(resultSet.getString("name"));
                types.add(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get all types", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return types;
    }
}
