package agentarium.attributes.results.databases;

import utils.RandomStringGenerator;

import java.lang.reflect.InvocationTargetException;

public abstract class AttributeSetResultsDatabaseFactory {
    private static Class<?> databaseClass = null;

    public static <T extends AttributeSetResultsDatabase> void setDatabaseClass(Class<T> databaseClass) {
        AttributeSetResultsDatabaseFactory.databaseClass = databaseClass;
    }

    public static void setDatabaseToMemoryBased() {
        setDatabaseClass(MemoryBasedAttributeSetResultsDatabase.class);
    }

    public static void setDatabaseToDiskBased() {
        setDatabaseClass(DiskBasedAttributeSetResultsDatabase.class);
    }

    public static AttributeSetResultsDatabase createDatabase() {
        // If no database class is set, default to disk-based database
        if (databaseClass == null) {
            setDatabaseToDiskBased();
        }

        try {
            // Use reflection to instantiate the configured database class
            AttributeSetResultsDatabase database = (AttributeSetResultsDatabase) databaseClass.getDeclaredConstructor().newInstance();
            // Generate a unique path for the database
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
