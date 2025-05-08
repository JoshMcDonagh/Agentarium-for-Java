package agentarium.attributes.results.databases;

import utils.RandomStringGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * Factory class for creating instances of {@link AttributeSetResultsDatabase}.
 *
 * <p>Supports dynamic switching between in-memory and disk-based result storage
 * via static configuration methods. This allows the simulation to use different
 * result backends without changing core logic.
 */
public abstract class AttributeSetResultsDatabaseFactory {

    /** The class used to instantiate new database instances */
    private static Class<?> databaseClass = null;

    /** Optional supplier used for test-time custom injection */
    private static Supplier<AttributeSetResultsDatabase> customFactory = null;

    /**
     * Manually sets the class used for creating result databases.
     *
     * @param databaseClass a class that extends {@link AttributeSetResultsDatabase} and has a no-argument constructor
     * @param <T> the database type
     */
    public static <T extends AttributeSetResultsDatabase> void setDatabaseClass(Class<T> databaseClass) {
        AttributeSetResultsDatabaseFactory.databaseClass = databaseClass;
    }

    /**
     * Sets a custom factory to be used instead of the default class-based instantiation.
     * This is useful for testing.
     *
     * @param factory the custom supplier of {@link AttributeSetResultsDatabase} instances
     */
    public static void setCustomFactory(Supplier<AttributeSetResultsDatabase> factory) {
        customFactory = factory;
    }

    /**
     * Clears any custom factory that was previously set, reverting to default behaviour.
     */
    public static void clearCustomFactory() {
        customFactory = null;
    }

    /**
     * Sets the results database to use an in-memory backend.
     * Useful for lightweight or test simulations.
     */
    public static void setDatabaseToMemoryBased() {
        setDatabaseClass(MemoryBasedAttributeSetResultsDatabase.class);
    }

    /**
     * Sets the results database to use a disk-based SQLite backend.
     * Useful for full-scale runs or persistent storage.
     */
    public static void setDatabaseToDiskBased() {
        setDatabaseClass(DiskBasedAttributeSetResultsDatabase.class);
    }

    /**
     * Creates a new instance of the configured results database.
     *
     * <p>If no database class has been configured, the default is a disk-based database.
     * A unique database path is generated automatically.
     *
     * @return a new {@link AttributeSetResultsDatabase} instance, or {@code null} on error
     */
    public static AttributeSetResultsDatabase createDatabase() {
        // Use the custom factory if one has been provided (e.g. during tests)
        if (customFactory != null) {
            return customFactory.get();
        }

        // Default to disk-based if not already set
        if (databaseClass == null) {
            setDatabaseToDiskBased();
        }

        try {
            // Instantiate the configured database class using reflection
            AttributeSetResultsDatabase database =
                    (AttributeSetResultsDatabase) databaseClass.getDeclaredConstructor().newInstance();

            // Assign a unique filename as the database path (even for memory-based, for traceability)
            database.setDatabasePath(RandomStringGenerator.generateUniqueRandomString(20) + ".db");

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
