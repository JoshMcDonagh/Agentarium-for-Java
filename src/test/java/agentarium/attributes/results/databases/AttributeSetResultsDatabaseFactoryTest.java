package agentarium.attributes.results.databases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AttributeSetResultsDatabaseFactory}.
 *
 * <p>Verifies default database type selection, dynamic class assignment,
 * and use of custom factories.
 */
public class AttributeSetResultsDatabaseFactoryTest {

    @AfterEach
    public void tearDown() {
        // Reset factory state after each test to avoid cross-test contamination
        AttributeSetResultsDatabaseFactory.clearCustomFactory();
        AttributeSetResultsDatabaseFactory.setDatabaseToDiskBased();
    }

    @Test
    public void testDefaultCreatesDiskDatabaseIfUnset() {
        // Clear any existing configuration
        AttributeSetResultsDatabaseFactory.setDatabaseClass(null);

        AttributeSetResultsDatabase db = AttributeSetResultsDatabaseFactory.createDatabase();

        assertNotNull(db);
        assertTrue(db instanceof DiskBasedAttributeSetResultsDatabase);
        assertNotNull(db.getDatabasePath());
        assertTrue(db.getDatabasePath().endsWith(".db"));
    }

    @Test
    public void testCreateMemoryBasedDatabase() {
        AttributeSetResultsDatabaseFactory.setDatabaseToMemoryBased();
        AttributeSetResultsDatabase db = AttributeSetResultsDatabaseFactory.createDatabase();

        assertNotNull(db);
        assertTrue(db instanceof MemoryBasedAttributeSetResultsDatabase);
    }

    @Test
    public void testCustomFactoryIsUsed() {
        AttributeSetResultsDatabase mockDb = new MemoryBasedAttributeSetResultsDatabase();
        AttributeSetResultsDatabaseFactory.setCustomFactory(() -> mockDb);

        AttributeSetResultsDatabase result = AttributeSetResultsDatabaseFactory.createDatabase();

        assertSame(mockDb, result, "Custom factory result should be returned");
    }

    @Test
    public void testCustomFactoryClearsCorrectly() {
        AttributeSetResultsDatabase mockDb = new MemoryBasedAttributeSetResultsDatabase();
        AttributeSetResultsDatabaseFactory.setCustomFactory(() -> mockDb);

        // Create once with custom factory
        AttributeSetResultsDatabase db1 = AttributeSetResultsDatabaseFactory.createDatabase();
        assertSame(mockDb, db1);

        // Clear and ensure it no longer returns the mock
        AttributeSetResultsDatabaseFactory.clearCustomFactory();
        AttributeSetResultsDatabaseFactory.setDatabaseToMemoryBased();
        AttributeSetResultsDatabase db2 = AttributeSetResultsDatabaseFactory.createDatabase();
        assertNotSame(mockDb, db2);
    }

    @Test
    public void testCreateReturnsNullOnInvalidClass() {
        AttributeSetResultsDatabaseFactory.setDatabaseClass(InvalidDatabase.class);

        AttributeSetResultsDatabase db = AttributeSetResultsDatabaseFactory.createDatabase();

        assertNull(db, "Should return null if instantiation fails");
    }

    /**
     * Dummy class without no-arg constructor, used to trigger instantiation error.
     */
    private static class InvalidDatabase extends AttributeSetResultsDatabase {
        public InvalidDatabase(String fail) {
        }

        @Override
        public <T> void addPropertyValue(String propertyName, T propertyValue) {}

        @Override
        public <T> void addPreEventValue(String preEventName, T preEventValue) {}

        @Override
        public <T> void addPostEventValue(String postEventName, T postEventValue) {}

        @Override
        public void setPropertyColumn(String propertyName, List<Object> propertyValues) {}

        @Override
        public void setPreEventColumn(String preEventName, List<Object> preEventValues) {}

        @Override
        public void setPostEventColumn(String postEventName, List<Object> postEventValues) {}

        @Override
        public List<Object> getPropertyColumnAsList(String propertyName) {
            return null;
        }

        @Override
        public List<Object> getPreEventColumnAsList(String preEventName) {
            return null;
        }

        @Override
        public List<Object> getPostEventColumnAsList(String postEventName) {
            return null;
        }
    }
}
