package attributedatabases;

import utilities.RandomStringGenerator;

import java.lang.reflect.InvocationTargetException;

public class DatabaseFactory {
    private static Class<?> databaseClass = StorageBasedDatabase.class;

    public static <T extends AttributeResultsDatabase> void setDatabaseClass(Class<T> databaseClass) {
        DatabaseFactory.databaseClass = databaseClass;
    }

    public static <T extends AttributeResultsDatabase> T createDatabase() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T database = (T) databaseClass.getDeclaredConstructor().newInstance();
        database.setDatabasePath(RandomStringGenerator.generateRandomString(20) + ".db");
        return database;
    }
}
