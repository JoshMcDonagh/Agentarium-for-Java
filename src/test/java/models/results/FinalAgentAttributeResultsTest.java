package models.results;

import agents.Agent;
import agents.attributes.results.AgentAttributeResults;
import agents.attributes.results.AgentAttributeSetResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link FinalAgentAttributeResults} class.
 * These tests validate the functionality of storing, retrieving, merging, and managing
 * agent attribute results within the simulation.
 */
class FinalAgentAttributeResultsTest {

    // Instance of the class under test
    private FinalAgentAttributeResults finalAgentAttributeResults;

    // Mocked agents and their attribute results
    @Mock
    private Agent mockAgent1;

    @Mock
    private Agent mockAgent2;

    @Mock
    private AgentAttributeResults mockResults1;

    @Mock
    private AgentAttributeResults mockResults2;

    /**
     * Initialises mocks and sets up a {@link FinalAgentAttributeResults} instance
     * with two mocked agents before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock agent names and results
        when(mockAgent1.name()).thenReturn("Agent1");
        when(mockAgent2.name()).thenReturn("Agent2");
        when(mockAgent1.getResults()).thenReturn(mockResults1);
        when(mockAgent2.getResults()).thenReturn(mockResults2);

        // Create a FinalAgentAttributeResults instance with mocked agents
        finalAgentAttributeResults = new FinalAgentAttributeResults(List.of(mockAgent1, mockAgent2));
    }

    /**
     * Tests that the constructor correctly stores agent attribute results
     * in both the internal map and list.
     */
    @Test
    void testConstructorStoresResults() {
        // Verify results are stored correctly in the map and list
        assertEquals(mockResults1, finalAgentAttributeResults.getAgentAttributeResults("Agent1"));
        assertEquals(mockResults2, finalAgentAttributeResults.getAgentAttributeResults("Agent2"));
        assertEquals(2, finalAgentAttributeResults.getAgentAttributeResultsCount());
    }

    /**
     * Tests the {@link FinalAgentAttributeResults#mergeWith(FinalAgentAttributeResults)} method
     * to ensure it correctly merges results from another instance.
     */
    @Test
    void testMergeWith() {
        // Mock another FinalAgentAttributeResults instance
        Agent mockAgent3 = mock(Agent.class);
        AgentAttributeResults mockResults3 = mock(AgentAttributeResults.class);
        when(mockAgent3.name()).thenReturn("Agent3");
        when(mockAgent3.getResults()).thenReturn(mockResults3);

        FinalAgentAttributeResults other = new FinalAgentAttributeResults(List.of(mockAgent3));
        finalAgentAttributeResults.mergeWith(other);

        // Verify the results from the other instance are merged
        assertEquals(mockResults3, finalAgentAttributeResults.getAgentAttributeResults("Agent3"));
    }

    /**
     * Tests the retrieval of property values for a specific agent's attribute.
     */
    @Test
    void testGetAgentPropertyValues() {
        // Mock attribute results
        when(mockResults1.getAttributeResults("Attribute1")).thenReturn(mock(AgentAttributeSetResults.class));
        AgentAttributeSetResults mockAttributeResults = mock(AgentAttributeSetResults.class);
        when(mockResults1.getAttributeResults("Attribute1")).thenReturn(mockAttributeResults);
        when(mockAttributeResults.getPropertyValues("Property1")).thenReturn(List.of(1, 2, 3));

        List<Object> values = finalAgentAttributeResults.getAgentPropertyValues("Agent1", "Attribute1", "Property1");
        assertEquals(List.of(1, 2, 3), values);
    }

    /**
     * Tests the retrieval of pre-event trigger results for a specific agent's attribute.
     */
    @Test
    void testGetAgentPreEventTriggers() {
        // Mock pre-event triggers
        when(mockResults1.getAttributeResults("Attribute1")).thenReturn(mock(AgentAttributeSetResults.class));
        AgentAttributeSetResults mockAttributeSetResults = mock(AgentAttributeSetResults.class);
        when(mockResults1.getAttributeResults("Attribute1")).thenReturn(mockAttributeSetResults);
        when(mockAttributeSetResults.getPreEventTriggers("Event1")).thenReturn(List.of(true, false, true));

        List<Boolean> triggers = finalAgentAttributeResults.getAgentPreEventTriggers("Agent1", "Attribute1", "Event1");
        assertEquals(List.of(true, false, true), triggers);
    }

    /**
     * Tests the retrieval of post-event trigger results for a specific agent's attribute.
     */
    @Test
    void testGetAgentPostEventTriggers() {
        // Mock post-event triggers
        when(mockResults1.getAttributeResults("Attribute1")).thenReturn(mock(AgentAttributeSetResults.class));
        AgentAttributeSetResults mockAttributeSetResults = mock(AgentAttributeSetResults.class);
        when(mockResults1.getAttributeResults("Attribute1")).thenReturn(mockAttributeSetResults);
        when(mockAttributeSetResults.getPostEventTriggers("Event1")).thenReturn(List.of(false, true));

        List<Boolean> triggers = finalAgentAttributeResults.getAgentPostEventTriggers("Agent1", "Attribute1", "Event1");
        assertEquals(List.of(false, true), triggers);
    }

    /**
     * Tests the {@link FinalAgentAttributeResults#disconnectDatabases()} method
     * to ensure it clears all stored results and disconnects databases.
     */
    @Test
    void testDisconnectDatabases() {
        // Call disconnectDatabases
        finalAgentAttributeResults.disconnectDatabases();

        // Verify databases are cleared
        verify(mockResults1).disconnectDatabases();
        verify(mockResults2).disconnectDatabases();
        assertEquals(0, finalAgentAttributeResults.getAgentAttributeResultsCount());
    }
}
