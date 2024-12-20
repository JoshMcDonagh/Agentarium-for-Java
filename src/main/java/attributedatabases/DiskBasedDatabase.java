package attributedatabases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * Implementation of {@link AttributeResultsDatabase} that stores attribute results in a SQLite database on disk.
 * This class handles the creation, modification, and retrieval of data stored in three main tables:
 * properties, pre-events, and post-events.
 * <p>
 * Each table stores data columns corresponding to various attributes, ensuring that
 * attribute types are maintained and serialised/deserialised correctly.
 */
public class DiskBasedDatabase extends AttributeResultsDatabase {

    // Thread-safe list for active databases
    private static final List<DiskBasedDatabase> activeDatabases = Collections.synchronizedList(new ArrayList<>());
    private static boolean shutdownHookRegistered = false;

    // Names of the attribute tables
    private final String PROPERTIES_TABLE_NAME = "properties_table";
    private final String PRE_EVENTS_TABLE_NAME = "pre_events_table";
    private final String POST_EVENTS_TABLE_NAME = "post_events_table";

    // Maps to store the types of attributes for proper deserialisation
    private final Map<String, Class<?>> propertyClassesMap = new HashMap<>();
    private final Map<String, Class<?>> preEventClassesMap = new HashMap<>();
    private final Map<String, Class<?>> postEventClassesMap = new HashMap<>();

    // SQLite connection object
    private Connection connection;

    /**
     * Constructs a new instance of the {@link DiskBasedDatabase} class.
     * The registered instances will be disconnected automatically when the JVM shuts down.
     *
     * During the JVM shutdown, all active database connections are closed,
     * and associated database files are deleted if they exist.
     *
     * This design ensures that no database connections remain open unintentionally,
     * preventing potential resource leaks or file locks.
     */
    public DiskBasedDatabase() {
        synchronized (activeDatabases) {
            activeDatabases.add(this);
            if (!shutdownHookRegistered) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    // Create a copy of the list for iteration to avoid concurrent modification
                    List<DiskBasedDatabase> databasesSnapshot;
                    synchronized (activeDatabases) {
                        databasesSnapshot = new ArrayList<>(activeDatabases);
                    }
                    for (DiskBasedDatabase db : databasesSnapshot) {
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
     * Establishes a connection to the SQLite database and creates the necessary tables if they do not exist.
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
     * Closes the connection to the SQLite database, if open.
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

    /**
     * Adds a single property value to the properties table.
     *
     * @param propertyName  The name of the property.
     * @param propertyValue The value of the property to add.
     * @param <T>           The type of the property value.
     */
    @Override
    public <T> void addPropertyValue(String propertyName, T propertyValue) {
        propertyClassesMap.put(propertyName, propertyValue.getClass());
        ensureColumnExists(PROPERTIES_TABLE_NAME, propertyName);
        insertData(PROPERTIES_TABLE_NAME, propertyName, serialiseValue(propertyValue));
    }

    /**
     * Adds a single pre-event value to the pre-events table.
     *
     * @param preEventName  The name of the pre-event.
     * @param preEventValue The value of the pre-event to add.
     * @param <T>           The type of the pre-event value.
     */
    @Override
    public <T> void addPreEventValue(String preEventName, T preEventValue) {
        preEventClassesMap.put(preEventName, preEventValue.getClass());
        ensureColumnExists(PRE_EVENTS_TABLE_NAME, preEventName);
        insertData(PRE_EVENTS_TABLE_NAME, preEventName, serialiseValue(preEventValue));
    }

    /**
     * Adds a single post-event value to the post-events table.
     *
     * @param postEventName  The name of the post-event.
     * @param postEventValue The value of the post-event to add.
     * @param <T>            The type of the post-event value.
     */
    @Override
    public <T> void addPostEventValue(String postEventName, T postEventValue) {
        postEventClassesMap.put(postEventName, postEventValue.getClass());
        ensureColumnExists(POST_EVENTS_TABLE_NAME, postEventName);
        insertData(POST_EVENTS_TABLE_NAME, postEventName, serialiseValue(postEventValue));
    }

    /**
     * Replaces an entire column in the properties table with new values.
     *
     * @param propertyName  The name of the property column.
     * @param propertyValues The list of new values for the column.
     */
    @Override
    public void setPropertyColumn(String propertyName, List<Object> propertyValues) {
        propertyClassesMap.put(propertyName, propertyValues.get(0).getClass());
        setColumn(PROPERTIES_TABLE_NAME, propertyName, propertyValues);
    }

    /**
     * Sets a column in the pre-events table with the specified values.
     *
     * @param preEventName   The name of the pre-event column.
     * @param preEventValues A list of values to replace the column contents with.
     */
    @Override
    public void setPreEventColumn(String preEventName, List<Object> preEventValues) {
        // Update the class type map for the pre-event column
        preEventClassesMap.put(preEventName, preEventValues.get(0).getClass());
        // Replace the column data
        setColumn(PRE_EVENTS_TABLE_NAME, preEventName, preEventValues);
    }

    /**
     * Sets a column in the post-events table with the specified values.
     *
     * @param postEventName   The name of the post-event column.
     * @param postEventValues A list of values to replace the column contents with.
     */
    @Override
    public void setPostEventColumn(String postEventName, List<Object> postEventValues) {
        // Update the class type map for the post-event column
        postEventClassesMap.put(postEventName, postEventValues.get(0).getClass());
        // Replace the column data
        setColumn(POST_EVENTS_TABLE_NAME, postEventName, postEventValues);
    }

    /**
     * Retrieves the values of a column from the properties table as a list.
     *
     * @param propertyName The name of the property column.
     * @return A list of values retrieved from the column.
     */
    @Override
    public List<Object> getPropertyColumnAsList(String propertyName) {
        return retrieveColumn(PROPERTIES_TABLE_NAME, propertyName, propertyClassesMap.get(propertyName));
    }

    /**
     * Retrieves the values of a column from the pre-events table as a list.
     *
     * @param preEventName The name of the pre-event column.
     * @return A list of values retrieved from the column.
     */
    @Override
    public List<Object> getPreEventColumnAsList(String preEventName) {
        return retrieveColumn(PRE_EVENTS_TABLE_NAME, preEventName, preEventClassesMap.get(preEventName));
    }

    /**
     * Retrieves the values of a column from the post-events table as a list.
     *
     * @param postEventName The name of the post-event column.
     * @return A list of values retrieved from the column.
     */
    @Override
    public List<Object> getPostEventColumnAsList(String postEventName) {
        return retrieveColumn(POST_EVENTS_TABLE_NAME, postEventName, postEventClassesMap.get(postEventName));
    }

    /**
     * Ensures that a specific column exists in a given table.
     * If the column does not exist, it will be added to the table.
     *
     * @param tableName  The name of the table.
     * @param columnName The name of the column to ensure exists.
     */
    private void ensureColumnExists(String tableName, String columnName) {
        String addColumnSQL = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT;";
        try (PreparedStatement stmt = connection.prepareStatement(addColumnSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Ignore errors related to duplicate column names
            if (!e.getMessage().contains("duplicate column name")) {
                throw new RuntimeException("Error ensuring column exists '" + columnName + "': " + e.getMessage(), e);
            }
        }
    }

    /**
     * Creates the main attribute tables (properties, pre-events, post-events) if they do not already exist.
     */
    private void createAttributeTables() {
        createTable(PROPERTIES_TABLE_NAME);
        createTable(PRE_EVENTS_TABLE_NAME);
        createTable(POST_EVENTS_TABLE_NAME);
    }

    /**
     * Creates a table in the database if it does not already exist.
     *
     * @param tableName The name of the table to create.
     */
    private void createTable(String tableName) {
        String createSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY);";
        try (PreparedStatement stmt = connection.prepareStatement(createSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table '" + tableName + "': " + e.getMessage(), e);
        }
    }

    /**
     * Inserts a serialised value into a specific column of a table.
     *
     * @param tableName  The name of the table.
     * @param columnName The name of the column to insert data into.
     * @param value      The serialised value to insert.
     */
    private void insertData(String tableName, String columnName, String value) {
        ensureColumnExists(tableName, columnName);
        String insertSQL = "INSERT INTO " + tableName + " (" + columnName + ") VALUES (?);";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting data into '" + tableName + "': " + e.getMessage(), e);
        }
    }

    /**
     * Replaces the data in a column of a table with a new list of values.
     * The existing data in the column will be cleared before new values are added.
     *
     * @param tableName  The name of the table.
     * @param columnName The name of the column to replace.
     * @param values     The list of values to insert into the column.
     */
    private void setColumn(String tableName, String columnName, List<Object> values) {
        ensureColumnExists(tableName, columnName);
        String deleteSQL = "DELETE FROM " + tableName + ";";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
            // Clear the existing column data
            deleteStmt.executeUpdate();
            // Insert new values into the column
            for (Object value : values) {
                insertData(tableName, columnName, serialiseValue(value));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error replacing column '" + columnName + "': " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all values from a specific column in a table as a list.
     *
     * @param tableName  The name of the table.
     * @param columnName The name of the column to retrieve data from.
     * @param type       The class type to deserialise the values into.
     * @return A list of deserialised values from the column.
     */
    private List<Object> retrieveColumn(String tableName, String columnName, Class<?> type) {
        ensureColumnExists(tableName, columnName);
        String selectSQL = "SELECT " + columnName + " FROM " + tableName + ";";
        List<Object> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String value = rs.getString(columnName);
                if (value != null) {
                    results.add(type != null ? deserialiseValue(value, type) : value);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving column '" + columnName + "': " + e.getMessage(), e);
        }
        return new ArrayList<>(results); // Ensure consistent List type
    }

    /**
     * Serialises an object into a JSON string for storage in the database.
     *
     * @param value The object to serialise.
     * @return A JSON string representation of the object.
     */
    private static String serialiseValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serialising value: " + e.getMessage(), e);
        }
    }

    /**
     * Deserialises a JSON string back into an object of the specified class type.
     *
     * @param value The JSON string to deserialise.
     * @param type  The class type to deserialise into.
     * @return The deserialised object.
     */
    private Object deserialiseValue(String value, Class<?> type) {
        if (value == null) {
            return null; // Handle null values gracefully
        }
        if (type == null) {
            throw new IllegalArgumentException("Cannot deserialise: type is null");
        }
        try {
            return new ObjectMapper().readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserialising value: " + value + " with type: " + type.getName(), e);
        }
    }
}
