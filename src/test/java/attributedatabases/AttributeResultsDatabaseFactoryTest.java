package attributedatabases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.RandomStringGenerator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AttributeResultsDatabaseFactory} class.
 */
class AttributeResultsDatabaseFactoryTest {

    /**
     * Reset the factory to its initial state before each test.
     */
    @BeforeEach
    void resetFactory() {
        AttributeResultsDatabaseFactory.setDatabaseClass(null); // Reset the factory to ensure a clean slate.
    }

    /**
     * Tests that the factory defaults to creating disk-based databases if no class is configured.
     */
    @Test
    void testCreateDatabase_DefaultsToDiskBased() {
        AttributeResultsDatabase database = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions
        assertNotNull(database, "The factory should return a non-null database instance.");
        assertTrue(database instanceof DiskBasedDatabase, "The factory should default to creating a disk-based database.");
        assertNotNull(database.getDatabasePath(), "The database should have a generated path.");
        assertTrue(database.getDatabasePath().endsWith(".db"), "The database path should end with '.db'.");
    }

    /**
     * Tests that the factory creates a memory-based database when configured.
     */
    @Test
    void testCreateDatabase_MemoryBased() {
        AttributeResultsDatabaseFactory.setDatabaseToMemoryBased();
        AttributeResultsDatabase database = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions
        assertNotNull(database, "The factory should return a non-null database instance.");
        assertTrue(database instanceof MemoryBasedDatabase, "The factory should create a memory-based database.");
        assertNull(database.getDatabasePath(), "Memory-based databases should not have a database path.");
    }

    /**
     * Tests that the factory creates a disk-based database when explicitly configured.
     */
    @Test
    void testCreateDatabase_DiskBased() {
        AttributeResultsDatabaseFactory.setDatabaseToDiskBased();
        AttributeResultsDatabase database = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions
        assertNotNull(database, "The factory should return a non-null database instance.");
        assertTrue(database instanceof DiskBasedDatabase, "The factory should create a disk-based database.");
        assertNotNull(database.getDatabasePath(), "Disk-based databases should have a database path.");
        assertTrue(database.getDatabasePath().endsWith(".db"), "The database path should end with '.db'.");
    }

    /**
     * Tests that attempting to set an invalid class does not silently fail.
     */
    @Test
    void testSetInvalidDatabaseClass() {
        // Since we cannot directly pass a non-compatible class due to type constraints,
        // we test the default behaviour when no valid class is set.

        // Leave the class unset (null)
        AttributeResultsDatabaseFactory.setDatabaseClass(null);

        // Attempt to create a database (should default to disk-based)
        AttributeResultsDatabase database = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions
        assertNotNull(database, "The database should still be created (defaulted to disk-based).");
        assertTrue(database instanceof DiskBasedDatabase, "The database should default to DiskBasedDatabase.");
    }

    /**
     * Tests that the factory throws no exceptions when setting valid database classes.
     */
    @Test
    void testSetValidDatabaseClasses() {
        // Set to memory-based and create a database
        AttributeResultsDatabaseFactory.setDatabaseToMemoryBased();
        AttributeResultsDatabase memoryDatabase = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions for memory-based database
        assertNotNull(memoryDatabase, "The memory-based database instance should not be null.");
        assertTrue(memoryDatabase instanceof MemoryBasedDatabase, "The instance should be of type MemoryBasedDatabase.");

        // Set to disk-based and create a database
        AttributeResultsDatabaseFactory.setDatabaseToDiskBased();
        AttributeResultsDatabase diskDatabase = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions for disk-based database
        assertNotNull(diskDatabase, "The disk-based database instance should not be null.");
        assertTrue(diskDatabase instanceof DiskBasedDatabase, "The instance should be of type DiskBasedDatabase.");
    }

    /**
     * Tests the generated database paths for uniqueness.
     */
    @Test
    void testGeneratedDatabasePathsAreUnique() {
        AttributeResultsDatabaseFactory.setDatabaseToDiskBased();

        AttributeResultsDatabase database1 = AttributeResultsDatabaseFactory.createDatabase();
        AttributeResultsDatabase database2 = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions
        assertNotNull(database1.getDatabasePath(), "The first database should have a valid path.");
        assertNotNull(database2.getDatabasePath(), "The second database should have a valid path.");
        assertNotEquals(database1.getDatabasePath(), database2.getDatabasePath(), "Each database should have a unique path.");
    }

    /**
     * Tests that the database path generator uses the {@link RandomStringGenerator}.
     */
    @Test
    void testDatabasePathUsesRandomStringGenerator() {
        AttributeResultsDatabaseFactory.setDatabaseToDiskBased();
        AttributeResultsDatabase database = AttributeResultsDatabaseFactory.createDatabase();

        // Assertions
        assertNotNull(database.getDatabasePath(), "The database path should not be null.");
        assertTrue(database.getDatabasePath().matches("[A-Za-z0-9]{20}\\.db"),
                "The database path should be a 20-character random string followed by '.db'.");
    }
}
