package attributedatabases;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MemoryBasedDatabase}.
 */
public class MemoryBasedDatabaseTest {

    private final MemoryBasedDatabase database = new MemoryBasedDatabase();

    /**
     * Test adding and retrieving property values.
     */
    @Test
    void testAddAndRetrievePropertyValues() {
        String propertyName = "testProperty";
        List<Object> propertyValues = Arrays.asList(10, 20, 30);

        // Add property values
        for (Object value : propertyValues) {
            database.addPropertyValue(propertyName, value);
        }

        // Retrieve property values
        List<Object> retrievedValues = database.getPropertyColumnAsList(propertyName);

        // Assertions
        assertNotNull(retrievedValues, "Retrieved property values should not be null.");
        assertEquals(propertyValues, retrievedValues, "Retrieved property values should match the added values.");
    }

    /**
     * Test adding and retrieving pre-event values.
     */
    @Test
    void testAddAndRetrievePreEventValues() {
        String preEventName = "testPreEvent";
        List<Object> preEventValues = Arrays.asList(true, false, true);

        // Add pre-event values
        for (Object value : preEventValues) {
            database.addPreEventValue(preEventName, value);
        }

        // Retrieve pre-event values
        List<Object> retrievedValues = database.getPreEventColumnAsList(preEventName);

        // Assertions
        assertNotNull(retrievedValues, "Retrieved pre-event values should not be null.");
        assertEquals(preEventValues, retrievedValues, "Retrieved pre-event values should match the added values.");
    }

    /**
     * Test adding and retrieving post-event values.
     */
    @Test
    void testAddAndRetrievePostEventValues() {
        String postEventName = "testPostEvent";
        List<Object> postEventValues = Arrays.asList("Event1", "Event2", "Event3");

        // Add post-event values
        for (Object value : postEventValues) {
            database.addPostEventValue(postEventName, value);
        }

        // Retrieve post-event values
        List<Object> retrievedValues = database.getPostEventColumnAsList(postEventName);

        // Assertions
        assertNotNull(retrievedValues, "Retrieved post-event values should not be null.");
        assertEquals(postEventValues, retrievedValues, "Retrieved post-event values should match the added values.");
    }

    /**
     * Test replacing property column values.
     */
    @Test
    void testReplacePropertyColumnValues() {
        String propertyName = "testReplaceProperty";
        List<Object> initialValues = Arrays.asList(1, 2, 3);
        List<Object> newValues = Arrays.asList(4, 5, 6);

        // Add initial values
        for (Object value : initialValues) {
            database.addPropertyValue(propertyName, value);
        }

        // Replace with new values
        database.setPropertyColumn(propertyName, newValues);

        // Retrieve replaced values
        List<Object> retrievedValues = database.getPropertyColumnAsList(propertyName);

        // Assertions
        assertEquals(newValues, retrievedValues, "Retrieved property values should match the replaced values.");
    }

    /**
     * Test replacing pre-event column values.
     */
    @Test
    void testReplacePreEventColumnValues() {
        String preEventName = "testReplacePreEvent";
        List<Object> initialValues = Arrays.asList(true, false);
        List<Object> newValues = Arrays.asList(false, true, false);

        // Add initial values
        for (Object value : initialValues) {
            database.addPreEventValue(preEventName, value);
        }

        // Replace with new values
        database.setPreEventColumn(preEventName, newValues);

        // Retrieve replaced values
        List<Object> retrievedValues = database.getPreEventColumnAsList(preEventName);

        // Assertions
        assertEquals(newValues, retrievedValues, "Retrieved pre-event values should match the replaced values.");
    }

    /**
     * Test replacing post-event column values.
     */
    @Test
    void testReplacePostEventColumnValues() {
        String postEventName = "testReplacePostEvent";
        List<Object> initialValues = Arrays.asList("Initial1", "Initial2");
        List<Object> newValues = Arrays.asList("New1", "New2", "New3");

        // Add initial values
        for (Object value : initialValues) {
            database.addPostEventValue(postEventName, value);
        }

        // Replace with new values
        database.setPostEventColumn(postEventName, newValues);

        // Retrieve replaced values
        List<Object> retrievedValues = database.getPostEventColumnAsList(postEventName);

        // Assertions
        assertEquals(newValues, retrievedValues, "Retrieved post-event values should match the replaced values.");
    }

    /**
     * Test behaviour when adding values of an incorrect type.
     */
    @Test
    void testAddIncorrectType() {
        String propertyName = "testIncorrectType";

        // Add initial value
        database.addPropertyValue(propertyName, 10);

        // Attempt to add value of a different type
        assertThrows(IllegalArgumentException.class, () ->
                        database.addPropertyValue(propertyName, "StringValue"),
                "Adding a value of a different type should throw an exception.");
    }

    /**
     * Test behaviour when replacing a column that does not exist.
     */
    @Test
    void testReplaceNonexistentColumn() {
        String propertyName = "nonExistentProperty";

        // Attempt to replace a non-existent column
        assertThrows(IllegalArgumentException.class, () ->
                        database.setPropertyColumn(propertyName, Collections.singletonList(42)),
                "Replacing a non-existent column should throw an exception.");
    }
}