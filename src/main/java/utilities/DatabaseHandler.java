package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String DATABASE_URL = "jdbc:sqlite:agent_results.db"; // Path to SQLite database
    private static Connection connection = null;

    static {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("SQLite connection established.");
        } catch (SQLException e) {
            System.err.println("Failed to establish SQLite connection: " + e.getMessage());
        }
    }

    public static void createTable(String tableName, String tableSchema) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
            System.out.println("Table '" + tableName + "' created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating table '" + tableName + "': " + e.getMessage());
        }
    }

    public static void insertData(String tableName, String columns, String values, Object... params) {
        String insertSQL = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ");";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting data into table '" + tableName + "': " + e.getMessage());
        }
    }

    public static ResultSet queryData(String query, Object... params) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error querying data: " + e.getMessage());
            return null;
        }
    }

    public static void updateData(String updateSQL, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("SQLite connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing SQLite connection: " + e.getMessage());
        }
    }
}
