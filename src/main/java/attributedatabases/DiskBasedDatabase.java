package attributedatabases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link AttributeResultsDatabase} that stores attribute results in a SQLite database on disk.
 */
public class DiskBasedDatabase extends AttributeResultsDatabase {

    // Table names for properties, pre-events, and post-events
    private final String PROPERTIES_TABLE_NAME = "H4BsmxQdcGK9fBfR";
    private final String PRE_EVENTS_TABLE_NAME = "H9Ym3mPySXCXozNt";
    private final String POST_EVENTS_TABLE_NAME = "7rFbEPJPRQgb7Fyi";

    // Maps to store the class type of properties, pre-events, and post-events
    private final Map<String, Class<?>> propertyClassesMap = new HashMap<>();
    private final Map<String, Class<?>> preEventClassesMap = new HashMap<>();
    private final Map<String, Class<?>> postEventClassesMap = new HashMap<>();

    // Database connection
    private Connection connection;

    /**
     * Connects to the SQLite database at the specified path.
     * Creates necessary tables if they do not already exist.
     */
    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + getDatabasePath());
            createAttributeTables();
        } catch (SQLException e) {
            System.err.println("Failed to establish SQLite connection: " + e.getMessage());
        }
    }

    /**
     * Disconnects from the SQLite database.
     */
    @Override
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing SQLite connection: " + e.getMessage());
        }
    }

    /**
     * Adds a property value to the properties table.
     *
     * @param propertyName  Name of the property.
     * @param propertyValue Value of the property.
     * @param <T>           Type of the property value.
     */
    @Override
    public <T> void addPropertyValue(String propertyName, T propertyValue) {
        propertyClassesMap.put(propertyName, propertyValue.getClass());
        String type = propertyValue.getClass().getName();
        String serialisedValue = serialiseValue(propertyValue);
        insertData(PROPERTIES_TABLE_NAME, "property_name, value, type", "?, ?, ?", propertyName, serialisedValue, type);
    }

    /**
     * Adds a pre-event value to the pre-events table.
     *
     * @param preEventName  Name of the pre-event.
     * @param preEventValue Value of the pre-event.
     * @param <T>           Type of the pre-event value.
     */
    @Override
    public <T> void addPreEventValue(String preEventName, T preEventValue) {
        preEventClassesMap.put(preEventName, preEventValue.getClass());
        String type = preEventValue.getClass().getName();
        String serialisedValue = serialiseValue(preEventValue);
        insertData(PRE_EVENTS_TABLE_NAME, "event_name, value, type", "?, ?, ?", preEventName, serialisedValue, type);
    }

    /**
     * Adds a post-event value to the post-events table.
     *
     * @param postEventName  Name of the post-event.
     * @param postEventValue Value of the post-event.
     * @param <T>            Type of the post-event value.
     */
    @Override
    public <T> void addPostEventValue(String postEventName, T postEventValue) {
        postEventClassesMap.put(postEventName, postEventValue.getClass());
        String type = postEventValue.getClass().getName();
        String serialisedValue = serialiseValue(postEventValue);
        insertData(POST_EVENTS_TABLE_NAME, "event_name, value, type", "?, ?, ?", postEventName, serialisedValue, type);
    }

    /**
     * Replaces a column in the properties table with new values.
     */
    @Override
    public void setPropertyColumn(String propertyName, List<Object> propertyValues) {
        propertyClassesMap.put(propertyName, propertyValues.get(0).getClass());
        setColumn(PROPERTIES_TABLE_NAME, propertyName, propertyValues);
    }

    /**
     * Replaces a column in the pre-events table with new values.
     */
    @Override
    public void setPreEventColumn(String preEventName, List<Object> preEventValues) {
        preEventClassesMap.put(preEventName, preEventValues.get(0).getClass());
        setColumn(PRE_EVENTS_TABLE_NAME, preEventName, preEventValues);
    }

    /**
     * Replaces a column in the post-events table with new values.
     */
    @Override
    public void setPostEventColumn(String postEventName, List<Object> postEventValues) {
        postEventClassesMap.put(postEventName, postEventValues.get(0).getClass());
        setColumn(POST_EVENTS_TABLE_NAME, postEventName, postEventValues);
    }

    /**
     * Retrieves a property column as a list.
     */
    @Override
    public List<Object> getPropertyColumnAsList(String propertyName) {
        return getColumnAsList(PROPERTIES_TABLE_NAME, propertyName, propertyClassesMap.get(propertyName));
    }

    /**
     * Retrieves a pre-event column as a list.
     */
    @Override
    public List<Object> getPreEventColumnAsList(String preEventName) {
        return getColumnAsList(PRE_EVENTS_TABLE_NAME, preEventName, preEventClassesMap.get(preEventName));
    }

    /**
     * Retrieves a post-event column as a list.
     */
    @Override
    public List<Object> getPostEventColumnAsList(String postEventName) {
        return getColumnAsList(POST_EVENTS_TABLE_NAME, postEventName, postEventClassesMap.get(postEventName));
    }

    // Helper method to ensure the database connection is active
    private void ensureConnection() {
        if (connection == null) {
            throw new IllegalStateException("No database connection established.");
        }
    }

    // Creates a new table in the database
    private void createTable(String tableName, String tableSchema) {
        ensureConnection();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error creating table '" + tableName + "': " + e.getMessage());
        }
    }

    // Creates attribute tables for properties, pre-events, and post-events
    private void createAttributeTables() {
        createTable(PROPERTIES_TABLE_NAME, "id INTEGER PRIMARY KEY, property_name TEXT, value TEXT, type TEXT");
        createTable(PRE_EVENTS_TABLE_NAME, "id INTEGER PRIMARY KEY, event_name TEXT, value TEXT, type TEXT");
        createTable(POST_EVENTS_TABLE_NAME, "id INTEGER PRIMARY KEY, event_name TEXT, value TEXT, type TEXT");
    }

    // Inserts data into a table
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

    // Serialises complex values into JSON
    private static String serialiseValue(Object value) {
        if (value instanceof Number || value instanceof Boolean || value instanceof String) {
            return value.toString();
        }
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serialising value: " + e.getMessage(), e);
        }
    }

    // Replaces a column's data in a table, creating the column if it doesn't exist
    private <T> void setColumn(String tableName, String columnName, List<T> values) {
        ensureConnection();
        try {
            connection.setAutoCommit(false);

            // Ensure the column exists, create it if necessary
            String addColumnSQL = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT;";
            try (PreparedStatement addColumnStmt = connection.prepareStatement(addColumnSQL)) {
                addColumnStmt.executeUpdate();
            } catch (SQLException e) {
                // Ignore the error if the column already exists
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e; // Re-throw if it's a different error
                }
            }

            // Clear existing column data
            String clearSQL = "UPDATE " + tableName + " SET " + columnName + " = NULL;";
            try (PreparedStatement clearStmt = connection.prepareStatement(clearSQL)) {
                clearStmt.executeUpdate();
            }

            // Update each row with the new value
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


    // Retrieves a column as a list
    private <T> List<Object> getColumnAsList(String tableName, String columnName, Class<T> type) {
        ensureConnection();
        List<Object> result = new ArrayList<>();
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
            throw new RuntimeException("Error casting values to type " + type.getName() + ": " + e.getMessage(), e);
        }

        return result;
    }
}