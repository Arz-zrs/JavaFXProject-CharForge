package com.project.charforge.dao.base;

import com.project.charforge.util.SQLiteConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> {
    protected abstract T mapRow(ResultSet result) throws SQLException;

    // Returns multiple rows
    protected List<T> queryList(String sql, StatementBinder binder) {
        List<T> list = new ArrayList<>();

        //noinspection SqlSourceToSinkFlow
        try (Connection connection = SQLiteConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            binder.bind(statement);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    list.add(mapRow(result));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Returns a single row
    protected T querySingle(String sql, StatementBinder binder) {

        //noinspection SqlSourceToSinkFlow
        try (Connection connection = SQLiteConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            binder.bind(statement);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapRow(result);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // UPDATE operations
    protected int executeUpdate(String sql, StatementBinder binder) {
        try (Connection connection = SQLiteConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            binder.bind(statement);
            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Insert values into a table
    protected int executeInsert(String sql, StatementBinder binder) {
        try (Connection connection = SQLiteConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            binder.bind(statement);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method that operates on SQL parameters
    @FunctionalInterface
    protected interface StatementBinder {
        void bind(PreparedStatement statement) throws SQLException;

        static StatementBinder empty() {
            return _ -> {};
        }
    }
}
