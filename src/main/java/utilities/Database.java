package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

public class Database {
    private final String databasePath;
    private Connection connection = null;

    public Database(String databasePath) {
        this.databasePath = databasePath;
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        } catch (SQLException e) {
            System.err.println("Failed to establish SQLite connection: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing SQLite connection: " + e.getMessage());
        }
    }

    public void createTable(String tableName, String tableSchema) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error creating table '" + tableName + "': " + e.getMessage());
        }
    }

    public void insertData(String tableName, String columns, String values, Object... params) {
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

    public ResultSet queryData(String query, Object... params) {
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

    public void updateData(String updateSQL, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
        }
    }

    public static String serialiseValue(Object value) {
        if (value instanceof Number || value instanceof Boolean || value instanceof String)
            return value.toString();

        // Serialise complex objects to JSON
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serialising property value: " + e.getMessage(), e);
        }
    }

    public static Object deserialiseValue(String value, String type) {
        try {
            return switch (type) {
                case "java.lang.Integer" -> Integer.parseInt(value);
                case "java.lang.Double" -> Double.parseDouble(value);
                case "java.lang.Boolean" -> Boolean.parseBoolean(value);
                case "java.lang.String" -> value;
                default ->
                    // Deserialise JSON for custom objects
                        new ObjectMapper().readValue(value, Class.forName(type));
            };
        } catch (Exception e) {
            throw new RuntimeException("Error deserialising value: " + e.getMessage(), e);
        }
    }
}
