package attributedatabases;

import utilities.RandomStringGenerator;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory class for creating instances of {@link AttributeResultsDatabase}.
 * Allows dynamic switching between different types of databases, such as memory-based or disk-based databases.
 */
public class AttributeResultsDatabaseFactory {

    // Class type for the database to be created (e.g., MemoryBasedDatabase or DiskBasedDatabase)
    private static Class<?> databaseClass = null;

    /**
     * Sets the database class to a specific implementation.
     *
     * @param databaseClass The class type extending {@link AttributeResultsDatabase}.
     * @param <T>           The type of the database class.
     */
    public static <T extends AttributeResultsDatabase> void setDatabaseClass(Class<T> databaseClass) {
        AttributeResultsDatabaseFactory.databaseClass = databaseClass;
    }

    /**
     * Configures the factory to produce memory-based databases.
     * Memory-based databases exist only in memory and are not persisted to disk.
     */
    public static void setDatabaseToMemoryBased() {
        setDatabaseClass(MemoryBasedDatabase.class);
    }

    /**
     * Configures the factory to produce disk-based databases.
     * Disk-based databases are persisted to disk and maintain data across sessions.
     */
    public static void setDatabaseToDiskBased() {
        setDatabaseClass(DiskBasedDatabase.class);
    }

    /**
     * Creates an instance of the currently configured {@link AttributeResultsDatabase}.
     * If no database class is configured, defaults to creating a disk-based database.
     *
     * @return An instance of {@link AttributeResultsDatabase}, or {@code null} if an error occurs.
     */
    public static AttributeResultsDatabase createDatabase() {
        // If no database class is set, default to disk-based database
        if (databaseClass == null) {
            setDatabaseToDiskBased();
        }

        try {
            // Use reflection to instantiate the configured database class
            AttributeResultsDatabase database = (AttributeResultsDatabase) databaseClass.getDeclaredConstructor().newInstance();
            // Generate a unique path for the database
            database.setDatabasePath(RandomStringGenerator.generateRandomString(20) + ".db");
            return database;
        } catch (NoSuchMethodException e) {
            System.err.println("Error: The specified database class does not have a no-argument constructor.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Error: Failed to invoke the constructor of the database class.");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Error: Failed to instantiate the database class.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("Error: Constructor of the database class is not accessible.");
            e.printStackTrace();
        }
        return null;
    }
}
