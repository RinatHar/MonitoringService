package org.kharisov.repos.databaseImpls;

import org.kharisov.dtos.ReadingTypeDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReadingTypeDbRepo extends BaseDbRepo<Long, ReadingTypeDto> {

    @Override
    public Optional<ReadingTypeDto> add(ReadingTypeDto dto) {
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
        }
        return Optional.empty();
    }

    public Optional<ReadingTypeDto> getByName(String name) {
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
        }
        return Optional.empty();
    }

    @Override
    public List<ReadingTypeDto> getAll() {
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
        }

        return types;
    }
}
