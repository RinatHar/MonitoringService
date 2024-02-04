package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.EntryDto;

import java.sql.*;
import java.util.*;

public class AuditDbRepo extends BaseDbRepo<Long, EntryDto> {

    @Override
    public Optional<EntryDto> add(EntryDto dto) {
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
        }
        return Optional.empty();
    }

    public List<EntryDto> getEntriesByAccountNum(String accountNum) {
        List<EntryDto> entries = new ArrayList<>();
        String sql = "SELECT audit.id, action, audit.user_id FROM audit\n" +
                "    join users u on u.id = audit.user_id where u.account_num = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNum);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EntryDto entry = new EntryDto();
                    entry.setId(resultSet.getLong("id"));
                    entry.setUserId(resultSet.getLong("user_id"));
                    entry.setAction(resultSet.getString("action"));
                    entries.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get entry", e);
        }

        return entries;
    }

    @Override
    public List<EntryDto> getAll() {
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
        }

        return entries;
    }
}
