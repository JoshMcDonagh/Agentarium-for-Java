package attributedatabases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DiskBasedDatabase} class.
 * Ensures that the database implementation handles properties, pre-events, and post-events correctly
 * when storing and retrieving data.
 */
class DiskBasedDatabaseTest {

    private DiskBasedDatabase database;

    /**
     * Sets up a new instance of the DiskBasedDatabase and connects to it before each test.
     */
    @BeforeEach
    void setUp() {
        database = new DiskBasedDatabase();
        database.setDatabasePath("test_database.db");
        database.connect();
    }

    /**
     * Disconnects from the database and cleans up resources after each test.
     */
    @AfterEach
    void tearDown() {
        database.disconnect();
    }

    /**
     * Tests adding a property value to the database and retrieving it.
     */
    @Test
    void testAddPropertyValue() {
        String propertyName = "testProperty";
        int propertyValue = 42;

        // Add the property value
        database.addPropertyValue(propertyName, propertyValue);

        // Retrieve the property values as a list
        List<Object> retrievedValues = database.getPropertyColumnAsList(propertyName);

        // Assertions
        assertNotNull(retrievedValues, "The retrieved values should not be null.");
        assertEquals(1, retrievedValues.size(), "The size of the retrieved values list should be 1.");
        assertEquals(propertyValue, retrievedValues.get(0), "The retrieved value should match the inserted value.");
    }

    /**
     * Tests adding a pre-event value to the database and retrieving it.
     */
    @Test
    void testAddPreEventValue() {
        String preEventName = "testPreEvent";
        boolean preEventValue = true;

        // Add the pre-event value
        database.addPreEventValue(preEventName, preEventValue);

        // Retrieve the pre-event values as a list
        List<Object> retrievedValues = database.getPreEventColumnAsList(preEventName);

        // Assertions
        assertNotNull(retrievedValues, "The retrieved values should not be null.");
        assertEquals(1, retrievedValues.size(), "The size of the retrieved values list should be 1.");
        assertEquals(preEventValue, retrievedValues.get(0), "The retrieved value should match the inserted value.");
    }

    /**
     * Tests adding a post-event value and retrieving it.
     */
    @Test
    void testAddPostEventValue() {
        String postEventName = "testPostEvent";
        String postEventValue = "Event Triggered";

        // Add the post-event value
        database.addPostEventValue(postEventName, postEventValue);

        // Retrieve the post-event values as a list
        List<Object> retrievedValues = database.getPostEventColumnAsList(postEventName);

        // Assertions
        assertNotNull(retrievedValues, "The retrieved values should not be null.");
        assertEquals(1, retrievedValues.size(), "The size of the retrieved values list should be 1.");
        assertEquals(postEventValue, retrievedValues.get(0).toString().trim(),
                "The retrieved value should match the inserted value.");
    }

    /**
     * Utility method to normalize and compare two lists.
     *
     * @param expected the expected list of values
     * @param actual   the actual list of values retrieved
     * @param message  the assertion message
     */
    private void assertNormalizedIterableEquals(List<Object> expected, List<Object> actual, String message) {
        assertNotNull(actual, "The retrieved values should not be null.");
        assertIterableEquals(expected, actual, message);
    }

    /**
     * Tests replacing a column in the properties table and retrieving its values.
     */
    @Test
    void testSetPropertyColumn() {
        String propertyName = "testPropertyColumn";
        List<Object> propertyValues = Arrays.asList(10, 20, 30);

        // Replace the column with new values
        database.setPropertyColumn(propertyName, propertyValues);

        // Retrieve the updated property column as a list
        List<Object> retrievedValues = database.getPropertyColumnAsList(propertyName);

        // Compare the lists
        assertNormalizedIterableEquals(propertyValues, retrievedValues,
                "The retrieved values should match the updated values.");
    }

    /**
     * Tests replacing a column in the pre-events table and retrieving its values.
     */
    @Test
    void testSetPreEventColumn() {
        String preEventName = "testPreEventColumn";
        List<Object> preEventValues = Arrays.asList(true, false, true);

        // Replace the column with new values
        database.setPreEventColumn(preEventName, preEventValues);

        // Retrieve the updated pre-event column as a list
        List<Object> retrievedValues = database.getPreEventColumnAsList(preEventName);

        // Compare the lists
        assertNormalizedIterableEquals(preEventValues, retrievedValues,
                "The retrieved values should match the updated values.");
    }

    /**
     * Tests replacing a column in the post-events table and retrieving its values.
     */
    @Test
    void testSetPostEventColumn() {
        String postEventName = "testPostEventColumn";
        List<Object> postEventValues = Arrays.asList("Event1", "Event2", "Event3");

        // Replace the column with new values
        database.setPostEventColumn(postEventName, postEventValues);

        // Retrieve the updated post-event column as a list
        List<Object> retrievedValues = database.getPostEventColumnAsList(postEventName);

        // Normalize and compare lists
        assertNormalizedIterableEquals(postEventValues, retrievedValues,
                "The retrieved values should match the updated values.");
    }

    /**
     * Tests retrieving values from a non-existent column.
     */
    @Test
    void testGetColumnAsListForNonExistentColumn() {
        String nonExistentColumn = "nonExistentColumn";

        // Attempt to retrieve values from a non-existent column
        List<Object> retrievedValues = database.getPropertyColumnAsList(nonExistentColumn);

        // Assertions
        assertNotNull(retrievedValues, "The retrieved values should not be null.");
        assertTrue(retrievedValues.isEmpty(), "The retrieved values should be an empty list for a non-existent column.");
    }
}
