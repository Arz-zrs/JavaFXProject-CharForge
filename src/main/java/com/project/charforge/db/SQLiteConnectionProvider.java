package com.project.charforge.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnectionProvider implements ConnectionProvider {
    private static final String url = "jdbc:sqlite:charforge.db";

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON;");
        }
        return connection;
    }
}
