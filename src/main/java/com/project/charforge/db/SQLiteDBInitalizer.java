package com.project.charforge.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDBInitalizer {
    private static final String SCHEMA_PATH = "com/project/charforge/data/charforge.sql";

    public static void initialize() {
        try (Connection conn = SQLiteConnectionProvider.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Initializing Database from SQL Script...");

            // 1. Baca file SQL dari Resources
            String sqlScript = loadSqlScript();

            // 2. Pecah script menjadi perintah-perintah terpisah (delimiter ';')
            // SQLite JDBC biasanya tidak bisa menjalankan file script panjang sekaligus
            String[] commands = sqlScript.split(";");

            // 3. Eksekusi setiap perintah
            conn.setAutoCommit(false); // Gunakan transaksi biar aman

            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }

            conn.commit();
            System.out.println("Database initialization complete.");

        } catch (SQLException | IOException e) {
            e.getStackTrace();
            System.err.println("Gagal inisialisasi database: " + e.getMessage());
        }
    }

    private static String loadSqlScript() throws IOException {
        try (InputStream is = SQLiteDBInitalizer.class.getResourceAsStream(SCHEMA_PATH)) {
            if (is == null) {
                throw new IOException("File SQL tidak ditemukan di: " + SCHEMA_PATH);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
