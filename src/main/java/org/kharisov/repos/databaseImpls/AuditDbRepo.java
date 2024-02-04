package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.EntryDto;

import java.sql.*;
import java.util.*;

/**
 * Класс AuditDbRepo представляет собой репозиторий для работы с записями аудита в базе данных.
 * Он наследуется от базового класса репозитория BaseDbRepo и реализует его абстрактные методы.
 */
public class AuditDbRepo extends BaseDbRepo<Long, EntryDto> {

    /**
     * Конструктор класса AuditDbRepo.
     *
     * @param connectionPool Пул соединений с базой данных.
     */
    public AuditDbRepo(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    /**
     * Добавляет запись аудита в базу данных и возвращает ее.
     *
     * @param dto Объект DTO записи аудита для добавления в базу данных.
     * @return Объект DTO записи аудита, добавленной в базу данных, или пустой Optional, если добавление не удалось.
     */
    @Override
    public Optional<EntryDto> add(EntryDto dto) {
        Connection connection = connectionPool.getConnectionFromPool();
        String sql = "INSERT INTO audit (action, user_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getAction());
            statement.setLong(2, dto.getUserId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setId(generatedKeys.getLong(1));
                    return Optional.of(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot add entry", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }
        return Optional.empty();
    }

    /**
     * Возвращает все записи аудита для указанного номера счета.
     *
     * @param accountNum Номер счета, для которого требуется получить записи аудита.
     * @return Список объектов DTO записей аудита для указанного номера счета.
     */
    public List<EntryDto> getEntriesByAccountNum(String accountNum) {
        Connection connection = connectionPool.getConnectionFromPool();
        List<EntryDto> entries = new ArrayList<>();
        String sql = "SELECT audit.id, action, audit.user_id, account_num FROM audit\n" +
                "    join users u on u.id = audit.user_id where u.account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EntryDto entry = new EntryDto();
                    entry.setId(resultSet.getLong("id"));
                    entry.setUserId(resultSet.getLong("user_id"));
                    entry.setAction(resultSet.getString("action"));
                    entry.setAccountNum(resultSet.getString("account_num"));
                    entries.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get entry", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return entries;
    }

    /**
     * Возвращает все записи аудита из базы данных.
     *
     * @return Список всех объектов DTO записей аудита из базы данных.
     */
    @Override
    public List<EntryDto> getAll() {
        Connection connection = connectionPool.getConnectionFromPool();
        List<EntryDto> entries = new ArrayList<>();
        String sql = "SELECT audit.id, action, audit.user_id, account_num FROM audit\n" +
                "    join users u on u.id = audit.user_id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                EntryDto entry = new EntryDto();
                entry.setId(resultSet.getLong("id"));
                entry.setUserId(resultSet.getLong("user_id"));
                entry.setAction(resultSet.getString("action"));
                entry.setAccountNum(resultSet.getString("account_num"));
                entries.add(entry);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get all entries", e);
        } finally {
            connectionPool.returnConnectionToPool(connection);
        }

        return entries;
    }
}

