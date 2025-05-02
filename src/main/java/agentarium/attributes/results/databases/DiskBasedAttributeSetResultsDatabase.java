package agentarium.attributes.results.databases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.*;

public class DiskBasedAttributeSetResultsDatabase extends AttributeSetResultsDatabase {
    private static final List<DiskBasedAttributeSetResultsDatabase> activeDatabases = Collections.synchronizedList(new ArrayList<>());
    private static boolean shutdownHookRegistered = false;

    private final String PROPERTIES_TABLE_NAME = "properties_table";
    private final String PRE_EVENTS_TABLE_NAME = "pre_events_table";
    private final String POST_EVENTS_TABLE_NAME = "post_events_table";

    private final Map<String, Class<?>> propertyClassesMap = new HashMap<>();
    private final Map<String, Class<?>> preEventClassesMap = new HashMap<>();
    private final Map<String, Class<?>> postEventClassesMap = new HashMap<>();

    private Connection connection;

    public DiskBasedAttributeSetResultsDatabase() {
        synchronized (activeDatabases) {
            activeDatabases.add(this);
            if (!shutdownHookRegistered) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    List<DiskBasedAttributeSetResultsDatabase> databasesSnapshot;
                    synchronized (activeDatabases) {
                        databasesSnapshot = new ArrayList<>(activeDatabases);
                    }
                    for (DiskBasedAttributeSetResultsDatabase db : databasesSnapshot) {
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

    @Override
    public void setPropertyColumn(String propertyName, List<Object> propertyValues) {
        propertyClassesMap.put(propertyName, propertyValues.get(0).getClass());
        setColumn(PROPERTIES_TABLE_NAME, propertyName, propertyValues);
    }

    @Override
    public void setPreEventColumn(String preEventName, List<Object> preEventValues) {
        preEventClassesMap.put(preEventName, preEventValues.get(0).getClass());
        setColumn(PRE_EVENTS_TABLE_NAME, preEventName, preEventValues);
    }

    @Override
    public void setPostEventColumn(String postEventName, List<Object> postEventValues) {
        postEventClassesMap.put(postEventName, postEventValues.get(0).getClass());
        setColumn(POST_EVENTS_TABLE_NAME, postEventName, postEventValues);
    }

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

    private void ensureColumnExists(String tableName, String columnName) {
        String addColumnSQL = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT;";
        try (PreparedStatement stmt = connection.prepareStatement(addColumnSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate column name"))
                throw new RuntimeException("Error ensuring column exists '" + columnName + "': " + e.getMessage(), e);
        }
    }

    private void createAttributeTables() {
        createTable(PROPERTIES_TABLE_NAME);
        createTable(PRE_EVENTS_TABLE_NAME);
        createTable(POST_EVENTS_TABLE_NAME);
    }

    private void createTable(String tableName) {
        String createSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY);";
        try (PreparedStatement stmt = connection.prepareStatement(createSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table '" + tableName + "': " + e.getMessage(), e);
        }
    }

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

    private List<Object> retrieveColumn(String tableName, String columnName, Class<?> type) {
        ensureColumnExists(tableName, columnName);
        String selectSQL = "SELECT " + columnName + " FROM " + tableName + ";";
        List<Object> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String value = rs.getString(columnName);
                if (value != null)
                    results.add(type != null ? deserialiseValue(value, type) : value);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving column '" + columnName + "': " + e.getMessage(), e);
        }
        return new ArrayList<>(results);
    }

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
