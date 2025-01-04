package models.results;

import agents.Agent;
import attributedatabases.AttributeResultsDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Results} abstract class.
 * Verifies key functionalities related to managing agent and model attribute results.
 */
class ResultsTest {

    private TestResults results; // Concrete subclass of Results for testing purposes.

    @Mock
    private FinalAgentAttributeResults mockFinalAgentAttributeResults;

    @Mock
    private FinalModelAttributeResults mockFinalModelAttributeResults;

    @Mock
    private AttributeResultsDatabase mockAttributeResultsDatabase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a concrete instance of the abstract Results class for testing.
        results = new TestResults();

        // Mock final results for agents and models.
        when(mockFinalAgentAttributeResults.getAgentPropertyValues("Agent1", "Attribute1", "Property1"))
                .thenReturn(List.of(10, 20, 30));
        when(mockFinalModelAttributeResults.getModelPropertyValues("Attribute1", "Property1"))
                .thenReturn(List.of(100, 200, 300));
    }

    /**
     * Tests setting and retrieving agent names.
     */
    @Test
    void testSetAndGetAgentNames() {
        Agent mockAgent1 = mock(Agent.class);
        Agent mockAgent2 = mock(Agent.class);
        when(mockAgent1.name()).thenReturn("Agent1");
        when(mockAgent2.name()).thenReturn("Agent2");

        results.setAgentNames(List.of(mockAgent1, mockAgent2));
        List<String> agentNames = results.getAgentNames();

        assertEquals(List.of("Agent1", "Agent2"), agentNames, "Agent names should match the mock data.");
    }

    /**
     * Tests setting and retrieving agent property values.
     */
    @Test
    void testGetAgentPropertyValues() {
        results.setFinalAgentAttributeResults(mockFinalAgentAttributeResults);

        List<Object> values = results.getAgentPropertyValues("Agent1", "Attribute1", "Property1");

        assertEquals(List.of(10, 20, 30), values, "Property values for the agent should match the mock data.");
    }

    /**
     * Tests retrieval of model property values.
     */
    @Test
    void testGetModelPropertyValues() {
        results.setFinalModelAttributes(mockFinalModelAttributeResults);

        List<Object> values = results.getModelPropertyValues("Attribute1", "Property1");

        assertEquals(List.of(100, 200, 300), values, "Property values for the model should match the mock data.");
    }

    /**
     * Tests disconnecting all raw databases.
     */
    @Test
    void testDisconnectRawDatabases() {
        results.setFinalAgentAttributeResults(mockFinalAgentAttributeResults);
        results.setFinalModelAttributes(mockFinalModelAttributeResults);

        results.disconnectRawDatabases();

        verify(mockFinalAgentAttributeResults, times(1)).disconnectDatabases();
        verify(mockFinalModelAttributeResults, times(1)).disconnectDatabases();
    }

    /**
     * Tests the behaviour of accumulated data disconnection using reflection.
     */
    @Test
    void testDisconnectAccumulatedDatabases() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to inject the mock database into accumulatedModelAttributeResultsDatabasesList
        Field listField = Results.class.getDeclaredField("accumulatedModelAttributeResultsDatabasesList");
        listField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<AttributeResultsDatabase> databaseList = (List<AttributeResultsDatabase>) listField.get(results);
        databaseList.add(mockAttributeResultsDatabase);

        // Use reflection to set the isAccumulatedModelAttributeDataConnected flag
        Field connectedField = Results.class.getDeclaredField("isAccumulatedModelAttributeDataConnected");
        connectedField.setAccessible(true);
        connectedField.setBoolean(results, true);

        // Call the method under test
        results.disconnectAccumulatedDatabases();

        // Verify that the disconnect() method was called on the mock database
        verify(mockAttributeResultsDatabase, times(1)).disconnect();

        // Assert that the database list is cleared
        assertTrue(databaseList.isEmpty(), "The accumulated databases list should be cleared after disconnection.");
    }

    /**
     * Concrete subclass of the abstract Results class for testing purposes.
     */
    private static class TestResults extends Results {

        @Override
        protected List<?> accumulateAgentPropertyResults(String attributeName, String propertyName, List<?> accumulatedValues, List<?> valuesToBeProcessed) {
            // Create a new list to combine both lists
            List<Object> combinedValues = new ArrayList<>();

            // Add all elements from accumulatedValues
            combinedValues.addAll((Collection<?>) accumulatedValues);

            // Add all elements from valuesToBeProcessed
            combinedValues.addAll((Collection<?>) valuesToBeProcessed);

            return combinedValues;
        }

        @Override
        protected List<?> accumulateAgentPreEventResults(String attributeName, String preEventName, List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
            // Create a new list to combine both lists
            List<Object> combinedValues = new ArrayList<>();

            // Add all elements from accumulatedValues
            combinedValues.addAll((Collection<?>) accumulatedValues);

            // Add all elements from valuesToBeProcessed
            combinedValues.addAll(valuesToBeProcessed);

            return combinedValues;
        }

        @Override
        protected List<?> accumulateAgentPostEventResults(String attributeName, String postEventName, List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
            // Create a new list to combine both lists
            List<Object> combinedValues = new ArrayList<>();

            // Add all elements from accumulatedValues
            combinedValues.addAll((Collection<?>) accumulatedValues);

            // Add all elements from valuesToBeProcessed
            combinedValues.addAll(valuesToBeProcessed);

            return combinedValues;
        }
    }
}