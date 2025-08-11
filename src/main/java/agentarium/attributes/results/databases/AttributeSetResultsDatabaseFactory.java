package agentarium.attributes.results.databases;

import utils.RandomStringGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * Factory class for creating instances of {@link AttributeSetResultsDatabase}.
 *
 * <p>Supports dynamic switching between in-memory and disk-based result storage
 * via static configuration methods. This allows the simulation to use different
 * result backends without changing core logic.
 */
public final class AttributeSetResultsDatabaseFactory {

    // Prevent instantiation
    private AttributeSetResultsDatabaseFactory() {}

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
        if (customFactory != null)
            return customFactory.get();

        if (databaseClass == null)
            setDatabaseToDiskBased();

        if (!AttributeSetResultsDatabase.class.isAssignableFrom(databaseClass))
            return null;

        @SuppressWarnings("unchecked")
        Class<? extends AttributeSetResultsDatabase> clazz =
                (Class<? extends AttributeSetResultsDatabase>) databaseClass;

        try {
            // Try to get a no-arg ctor; if it doesn't exist, return null (expected by the test)
            Constructor<? extends AttributeSetResultsDatabase> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true); // allow private/protected no-arg ctors

            AttributeSetResultsDatabase db = ctor.newInstance();

            // Give every instance a unique path (even if your memory impl ignores it)
            db.setDatabasePath(RandomStringGenerator.generateUniqueRandomString(20) + ".db");
            return db;

        } catch (NoSuchMethodException e) {
            // The invalid class in the test likely hits this path (e.g., non-static inner class)
            return null;
        } catch (ReflectiveOperationException e) {
            // Covers IllegalAccessException, InstantiationException, InvocationTargetException, etc.
            return null;
        }
    }
}
