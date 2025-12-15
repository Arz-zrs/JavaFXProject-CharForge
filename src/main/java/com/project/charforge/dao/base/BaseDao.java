package com.project.charforge.dao.base;

import com.project.charforge.db.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> {
    protected final ConnectionProvider connectionProvider;

    protected BaseDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected abstract T mapRow(ResultSet result) throws SQLException;

    // Returns multiple rows
    protected List<T> queryList(String sql, StatementBinder binder) {
        List<T> list = new ArrayList<>();

        //noinspection SqlSourceToSinkFlow
        try (Connection connection = connectionProvider.getConnection();
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
        try (Connection connection = connectionProvider.getConnection();
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

    // UPDATE / DELETE operations
    protected int executeQuery(String sql, StatementBinder binder) {
        // noinspection SqlSourceToSinkFlow
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            binder.bind(statement);
            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Insert values into a table
    protected int executeInsert(String sql, StatementBinder binder) {
        // noinspection SqlSourceToSinkFlow
        try (Connection connection = connectionProvider.getConnection();
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

    // Deletion with Foreign Key Constraint
    protected <R> R inTransaction(SqlTransaction<R> action) {
        try (Connection conn = connectionProvider.getConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                R result = action.execute(conn);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Transaction failed", e);
        }
    }

    @FunctionalInterface
    protected interface SqlTransaction<R> {
        R execute(Connection connection) throws SQLException;
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
