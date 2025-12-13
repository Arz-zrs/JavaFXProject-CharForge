package com.project.charforge.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// TODO: DIP this later using interface
// (also means refactor all DAOs)
public class SQLiteConnectionProvider {
    private static final String url = "jdbc:sqlite:charforge.db";
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON;");
        }
        return connection;
    }
}
