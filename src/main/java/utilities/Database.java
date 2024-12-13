package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String databasePath;
    private Connection connection = null;

    public Database(String databasePath) {
        this.databasePath = databasePath;
        connect();
    }

    private void ensureConnection() {
        if (connection == null) {
            throw new IllegalStateException("No database connection established.");
        }
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
        ensureConnection();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error creating table '" + tableName + "': " + e.getMessage());
        }
    }

    public void insertData(String tableName, String columns, String values, Object... params) {
        ensureConnection();
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
        ensureConnection();
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
        ensureConnection();
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
                default -> new ObjectMapper().readValue(value, Class.forName(type));
            };
        } catch (Exception e) {
            throw new RuntimeException("Error deserialising value: " + e.getMessage(), e);
        }
    }

    public <T> void replaceColumn(String tableName, String columnName, List<T> values) {
        ensureConnection();
        try {
            connection.setAutoCommit(false);

            String clearSQL = "UPDATE " + tableName + " SET " + columnName + " = NULL;";
            try (PreparedStatement clearStmt = connection.prepareStatement(clearSQL)) {
                clearStmt.executeUpdate();
            }

            String updateSQL = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE ROWID = ?;";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                for (int i = 0; i < values.size(); i++) {
                    updateStmt.setObject(1, values.get(i));
                    updateStmt.setInt(2, i + 1);
                    updateStmt.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error replacing column: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Failed to reset auto-commit: " + e.getMessage());
            }
        }
    }

    public <T> List<T> getColumnAsList(String tableName, String columnName, Class<T> type) {
        ensureConnection();
        List<T> result = new ArrayList<>();
        String querySQL = "SELECT " + columnName + " FROM " + tableName + ";";

        try (PreparedStatement queryStmt = connection.prepareStatement(querySQL);
             ResultSet resultSet = queryStmt.executeQuery()) {

            while (resultSet.next()) {
                Object value = resultSet.getObject(columnName);
                if (value != null) {
                    T castedValue = type.cast(value);
                    result.add(castedValue);
                } else {
                    result.add(null);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving column as list: " + e.getMessage());
        } catch (ClassCastException e) {
            throw new RuntimeException("Error casting retrieved values to type " + type.getName() + ": " + e.getMessage(), e);
        }

        return result;
    }
}
