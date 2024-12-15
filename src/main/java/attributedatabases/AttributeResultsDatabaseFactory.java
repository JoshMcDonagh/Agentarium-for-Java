package attributedatabases;

import utilities.RandomStringGenerator;

import java.lang.reflect.InvocationTargetException;

public class AttributeResultsDatabaseFactory {
    private static Class<?> databaseClass = null;

    public static <T extends AttributeResultsDatabase> void setDatabaseClass(Class<T> databaseClass) {
        AttributeResultsDatabaseFactory.databaseClass = databaseClass;
    }

    public static void setDatabaseToMemoryBased() {
        setDatabaseClass(MemoryBasedDatabase.class);
    }

    public static void setDatabaseToDiskBased() {
        setDatabaseClass(DiskBasedDatabase.class);
    }

    public static AttributeResultsDatabase createDatabase() {
        if (databaseClass == null)
            setDatabaseToDiskBased();

        try {
            AttributeResultsDatabase database = (AttributeResultsDatabase) databaseClass.getDeclaredConstructor().newInstance();
            database.setDatabasePath(RandomStringGenerator.generateRandomString(20) + ".db");
            return database;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
