package attributedatabases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import utilities.RandomStringGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageBasedDatabase extends AttributeResultsDatabase {
    private final String PROPERTIES_TABLE_NAME = "H4BsmxQdcGK9fBfR";
    private final String PRE_EVENTS_TABLE_NAME = "H9Ym3mPySXCXozNt";
    private final String POST_EVENTS_TABLE_NAME = "7rFbEPJPRQgb7Fyi";

    private final Map<String, Class<?>> propertyClassesMap = new HashMap<String, Class<?>>();

    private Connection connection;

    public StorageBasedDatabase() {
        super(RandomStringGenerator.generateRandomString(20) + ".db");
    }

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + getDatabasePath());
            createAttributeTables();
        } catch (SQLException e) {
            System.err.println("Failed to establish SQLite connection: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing SQLite connection: " + e.getMessage());
        }
    }

    @Override
    public <T> void addPropertyValue(String propertyName, T propertyValue) {
        propertyClassesMap.put(propertyName, propertyValue.getClass());
        String type = propertyValue.getClass().getName();
        String serialisedValue = serialiseValue(propertyValue);
        insertData(PROPERTIES_TABLE_NAME, "property_name, value, type", "?, ?, ?", propertyName, serialisedValue, type);
    }

    @Override
    public void addPreEventTrigger(String preEventName, boolean preEventTrigger) {
        insertData(PRE_EVENTS_TABLE_NAME, "event_name, triggered", "?, ?", preEventName, preEventTrigger);
    }

    @Override
    public void addPostEventTrigger(String postEventName, boolean postEventTrigger) {
        insertData(POST_EVENTS_TABLE_NAME, "event_name, triggered", "?, ?", postEventName, postEventTrigger);
    }

    @Override
    public <T> void replacePropertyColumn(String propertyName, List<T> propertyValues) {
        propertyClassesMap.put(propertyName, propertyValues.get(0).getClass());
        replaceColumn(PROPERTIES_TABLE_NAME, propertyName, propertyValues);
    }

    @Override
    public void replacePreEventTrigger(String preEventName, List<Boolean> preEventTriggers) {
        replaceColumn(PRE_EVENTS_TABLE_NAME, preEventName, preEventTriggers);
    }

    @Override
    public void replacePostEventTrigger(String postEventName, List<Boolean> postEventTriggers) {
        replaceColumn(POST_EVENTS_TABLE_NAME, postEventName, postEventTriggers);
    }

    @Override
    public List<?> getPropertyColumnAsList(String propertyName) {
        return getColumnAsList(PROPERTIES_TABLE_NAME, propertyName, propertyClassesMap.get(propertyName));
    }

    @Override
    public List<Boolean> getPreEventColumnAsList(String preEventName) {
        return getColumnAsList(PRE_EVENTS_TABLE_NAME, preEventName, Boolean.class);
    }

    @Override
    public List<Boolean> getPostEventColumnAsList(String postEventName) {
        return getColumnAsList(POST_EVENTS_TABLE_NAME, postEventName, Boolean.class);
    }

    private void ensureConnection() {
        if (connection == null) {
            throw new IllegalStateException("No database connection established.");
        }
    }

    private void createTable(String tableName, String tableSchema) {
        ensureConnection();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error creating table '" + tableName + "': " + e.getMessage());
        }
    }

    private void createAttributeTables() {
        createTable(PROPERTIES_TABLE_NAME, "id INTEGER PRIMARY KEY, property_name TEXT, value TEXT, type TEXT");
        createTable(PRE_EVENTS_TABLE_NAME, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
        createTable(POST_EVENTS_TABLE_NAME, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
    }

    private void insertData(String tableName, String columns, String values, Object... params) {
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

    private static String serialiseValue(Object value) {
        if (value instanceof Number || value instanceof Boolean || value instanceof String)
            return value.toString();

        // Serialise complex objects to JSON
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serialising property value: " + e.getMessage(), e);
        }
    }

    private <T> void replaceColumn(String tableName, String columnName, List<T> values) {
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

    private <T> List<T> getColumnAsList(String tableName, String columnName, Class<T> type) {
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
