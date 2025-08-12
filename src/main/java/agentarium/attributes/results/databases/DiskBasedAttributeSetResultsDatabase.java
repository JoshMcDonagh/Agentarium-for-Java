package agentarium.attributes.results.databases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * Concrete implementation of {@link AttributeSetResultsDatabase} that stores results
 * in a temporary SQLite database file.
 *
 * <p>Each run of the simulation creates a new SQLite database that is deleted on shutdown.
 * Values are serialised to JSON strings to support flexible data types.
 *
 * <p>This class supports both incremental (`addXValue`) and bulk (`setXColumn`) writes.
 */
public class DiskBasedAttributeSetResultsDatabase extends AttributeSetResultsDatabase {

    /** Thread-safe list of currently active databases to auto-disconnect on JVM shutdown */
    private static final List<DiskBasedAttributeSetResultsDatabase> activeDatabases = Collections.synchronizedList(new ArrayList<>());
    private static boolean shutdownHookRegistered = false;

    private static Class<?> firstNonNullClass(List<?> values) {
        if (values == null) return null;
        for (Object v : values) {
            if (v != null) return v.getClass();
        }
        return null;
    }

    private final String PROPERTIES_TABLE_NAME = "properties_table";
    private final String PRE_EVENTS_TABLE_NAME = "pre_events_table";
    private final String POST_EVENTS_TABLE_NAME = "post_events_table";

    private final Map<String, Class<?>> propertyClassesMap = new HashMap<>();
    private final Map<String, Class<?>> preEventClassesMap = new HashMap<>();
    private final Map<String, Class<?>> postEventClassesMap = new HashMap<>();

    private Connection connection;

    /** Registers this instance for automatic disconnect on JVM shutdown */
    public DiskBasedAttributeSetResultsDatabase() {
        synchronized (activeDatabases) {
            activeDatabases.add(this);
            if (!shutdownHookRegistered) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    List<DiskBasedAttributeSetResultsDatabase> snapshot;
                    synchronized (activeDatabases) {
                        snapshot = new ArrayList<>(activeDatabases);
                    }
                    for (DiskBasedAttributeSetResultsDatabase db : snapshot) {
                        try {
                            db.disconnect();
                        } catch (Exception e) {
                            System.err.println("Error while disconnecting database during shutdown: " + e.getMessage());
                        }
                    }
                }));
                shutdownHookRegistered = true;
            }
        }
    }

    /**
     * Establishes an SQLite connection and creates the required tables.
     */
    @Override
    public void connect() {
        if (connection != null)
            return;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + getDatabasePath());
            createAttributeTables();
        } catch (SQLException e) {
            System.err.println("Failed to establish SQLite connection: " + e.getMessage());
        }
    }

    /**
     * Closes the SQLite connection and deletes the database file.
     */
    @Override
    public void disconnect() {
        synchronized (activeDatabases) {
            if (connection == null)
                return;

            try {
                connection.close();
                String databasePath = getDatabasePath();
                File databaseFile = new File(databasePath);
                if (databaseFile.exists() && !databaseFile.delete())
                    System.err.println("Failed to delete database file: " + databasePath);
            } catch (SQLException e) {
                System.err.println("Error closing SQLite connection: " + e.getMessage());
            } finally {
                activeDatabases.remove(this);
            }
        }
    }

    // === Property/Event Value Recording (Per-Tick) ===

    @Override
    public <T> void addPropertyValue(String propertyName, T propertyValue) {
        propertyClassesMap.put(propertyName, propertyValue.getClass());
        ensureColumnExists(PROPERTIES_TABLE_NAME, propertyName);
        insertData(PROPERTIES_TABLE_NAME, propertyName, serialiseValue(propertyValue));
    }

    @Override
    public <T> void addPreEventValue(String preEventName, T preEventValue) {
        preEventClassesMap.put(preEventName, preEventValue.getClass());
        ensureColumnExists(PRE_EVENTS_TABLE_NAME, preEventName);
        insertData(PRE_EVENTS_TABLE_NAME, preEventName, serialiseValue(preEventValue));
    }

    @Override
    public <T> void addPostEventValue(String postEventName, T postEventValue) {
        postEventClassesMap.put(postEventName, postEventValue.getClass());
        ensureColumnExists(POST_EVENTS_TABLE_NAME, postEventName);
        insertData(POST_EVENTS_TABLE_NAME, postEventName, serialiseValue(postEventValue));
    }

    // === Bulk Column Replacement ===

    @Override
    public void setPropertyColumn(String propertyName, List<Object> propertyValues) {
        // Always ensure the column exists
        ensureColumnExists(PROPERTIES_TABLE_NAME, propertyName);

        // Infer and remember the element type if we can (skip if empty/all null)
        Class<?> inferred = firstNonNullClass(propertyValues);
        if (inferred != null) {
            propertyClassesMap.put(propertyName, inferred);
        }
        // Replace data (handles empty list by clearing the table)
        setColumn(PROPERTIES_TABLE_NAME, propertyName,
                (propertyValues == null) ? Collections.emptyList() : propertyValues);
    }

    @Override
    public void setPreEventColumn(String preEventName, List<Object> preEventValues) {
        ensureColumnExists(PRE_EVENTS_TABLE_NAME, preEventName);
        Class<?> inferred = firstNonNullClass(preEventValues);
        if (inferred != null) {
            preEventClassesMap.put(preEventName, inferred);
        }
        setColumn(PRE_EVENTS_TABLE_NAME, preEventName,
                (preEventValues == null) ? Collections.emptyList() : preEventValues);
    }

    @Override
    public void setPostEventColumn(String postEventName, List<Object> postEventValues) {
        ensureColumnExists(POST_EVENTS_TABLE_NAME, postEventName);
        Class<?> inferred = firstNonNullClass(postEventValues);
        if (inferred != null) {
            postEventClassesMap.put(postEventName, inferred);
        }
        setColumn(POST_EVENTS_TABLE_NAME, postEventName,
                (postEventValues == null) ? Collections.emptyList() : postEventValues);
    }

    // === Column Retrieval ===

    @Override
    public List<Object> getPropertyColumnAsList(String propertyName) {
        return retrieveColumn(PROPERTIES_TABLE_NAME, propertyName, propertyClassesMap.get(propertyName));
    }

    @Override
    public List<Object> getPreEventColumnAsList(String preEventName) {
        return retrieveColumn(PRE_EVENTS_TABLE_NAME, preEventName, preEventClassesMap.get(preEventName));
    }

    @Override
    public List<Object> getPostEventColumnAsList(String postEventName) {
        return retrieveColumn(POST_EVENTS_TABLE_NAME, postEventName, postEventClassesMap.get(postEventName));
    }

    // === Table & Column Management ===

    /** Ensures a table column exists, creating it if needed. */
    private void ensureColumnExists(String tableName, String columnName) {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate column name"))
                throw new RuntimeException("Error ensuring column exists '" + columnName + "': " + e.getMessage(), e);
        }
    }

    /** Creates base tables for properties and events if they do not exist. */
    private void createAttributeTables() {
        createTable(PROPERTIES_TABLE_NAME);
        createTable(PRE_EVENTS_TABLE_NAME);
        createTable(POST_EVENTS_TABLE_NAME);
    }

    /** Creates a new table with a primary key and no columns by default. */
    private void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table '" + tableName + "': " + e.getMessage(), e);
        }
    }

    /** Inserts a single value into a column. */
    private void insertData(String tableName, String columnName, String value) {
        ensureColumnExists(tableName, columnName);
        String sql = "INSERT INTO " + tableName + " (" + columnName + ") VALUES (?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting data into '" + tableName + "': " + e.getMessage(), e);
        }
    }

    /** Replaces all rows in a column with the provided values. */
    private void setColumn(String tableName, String columnName, List<Object> values) {
        ensureColumnExists(tableName, columnName);
        String deleteSQL = "DELETE FROM " + tableName + ";";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
            deleteStmt.executeUpdate();
            for (Object value : values)
                insertData(tableName, columnName, serialiseValue(value));
        } catch (SQLException e) {
            throw new RuntimeException("Error replacing column '" + columnName + "': " + e.getMessage(), e);
        }
    }

    /** Retrieves all rows from a column, deserialising them to the correct type. */
    private List<Object> retrieveColumn(String tableName, String columnName, Class<?> type) {
        ensureColumnExists(tableName, columnName);
        String sql = "SELECT " + columnName + " FROM " + tableName + ";";
        List<Object> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String value = rs.getString(columnName);
                if (value != null)
                    results.add(type != null ? deserialiseValue(value, type) : value);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving column '" + columnName + "': " + e.getMessage(), e);
        }
        return results;
    }

    // === JSON (De)serialisation Utilities ===

    private static String serialiseValue(Object value) {
        if (value == null)
            return null;
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serialising value: " + e.getMessage(), e);
        }
    }

    private Object deserialiseValue(String value, Class<?> type) {
        if (value == null)
            return null;
        if (type == null)
            throw new IllegalArgumentException("Cannot deserialise: type is null");
        try {
            return new ObjectMapper().readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserialising value: " + value + " with type: " + type.getName(), e);
        }
    }
}
