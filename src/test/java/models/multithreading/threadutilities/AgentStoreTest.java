package models.multithreading.threadutilities;

import agents.Agent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the `AgentStore` class.
 * Verifies the functionality for adding, retrieving, updating, and filtering agents.
 */
class AgentStoreTest {

    private AgentStore agentStore;

    @Mock
    private Agent mockAgent1;

    @Mock
    private Agent mockAgent2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialise the agent store to store copies of agents by default.
        agentStore = new AgentStore();

        // Mock agent behaviours
        when(mockAgent1.name()).thenReturn("Agent1");
        when(mockAgent1.duplicateWithoutRecords()).thenReturn(mockAgent1);

        when(mockAgent2.name()).thenReturn("Agent2");
        when(mockAgent2.duplicateWithoutRecords()).thenReturn(mockAgent2);
    }

    /**
     * Tests adding and retrieving a single agent.
     */
    @Test
    void testAddAndRetrieveAgent() {
        agentStore.addAgent(mockAgent1);

        // Verify the agent is retrievable by name.
        Agent retrievedAgent = agentStore.get("Agent1");
        assertEquals(mockAgent1, retrievedAgent, "The retrieved agent should match the added agent.");

        // Verify the agent exists in the store.
        assertTrue(agentStore.doesAgentExist("Agent1"), "The agent should exist in the store.");
    }

    /**
     * Tests adding and retrieving multiple agents.
     */
    @Test
    void testAddAndRetrieveMultipleAgents() {
        agentStore.addAgents(List.of(mockAgent1, mockAgent2));

        // Verify both agents are retrievable.
        assertEquals(mockAgent1, agentStore.get("Agent1"), "Agent1 should be retrievable from the store.");
        assertEquals(mockAgent2, agentStore.get("Agent2"), "Agent2 should be retrievable from the store.");

        // Verify the list of agents.
        List<Agent> agentsList = agentStore.getAgentsList();
        assertTrue(agentsList.contains(mockAgent1) && agentsList.contains(mockAgent2),
                "The agents list should contain all added agents.");
    }

    /**
     * Tests clearing the store.
     */
    @Test
    void testClearStore() {
        agentStore.addAgent(mockAgent1);
        agentStore.clear();

        // Verify the store is empty.
        assertNull(agentStore.get("Agent1"), "No agent should be retrievable after clearing the store.");
        assertTrue(agentStore.getAgentsList().isEmpty(), "The agents list should be empty after clearing the store.");
    }

    /**
     * Tests updating the store with another `AgentStore`.
     */
    @Test
    void testUpdateStore() {
        AgentStore otherAgentStore = new AgentStore();
        otherAgentStore.addAgent(mockAgent1);

        agentStore.update(otherAgentStore);

        // Verify the updated agent is present.
        assertEquals(mockAgent1, agentStore.get("Agent1"), "The agent should be updated from the other store.");
    }

    /**
     * Tests filtering agents using a predicate.
     */
    @Test
    void testFilterAgents() {
        agentStore.addAgents(List.of(mockAgent1, mockAgent2));

        // Filter to retrieve only Agent1.
        Predicate<Agent> filter = agent -> "Agent1".equals(agent.name());
        List<Agent> filteredAgents = agentStore.getFilteredAgents(filter);

        assertEquals(1, filteredAgents.size(), "Only one agent should match the filter criteria.");
        assertEquals(mockAgent1, filteredAgents.get(0), "The filtered agent should be Agent1.");
    }

    /**
     * Tests the behaviour of storing original agents versus copies.
     */
    @Test
    void testStoringAgentCopies() {
        AgentStore copyStore = new AgentStore(true); // Store copies
        AgentStore originalStore = new AgentStore(false); // Store original agents

        copyStore.addAgent(mockAgent1);
        originalStore.addAgent(mockAgent1);

        // Verify duplicateWithoutRecords is called when storing copies.
        verify(mockAgent1, times(1)).duplicateWithoutRecords();
    }
}
