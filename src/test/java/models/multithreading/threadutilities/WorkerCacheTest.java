package models.multithreading.threadutilities;

import agents.Agent;
import models.modelattributes.ModelAttributeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the `WorkerCache` class.
 * Verifies the functionality for managing agents, agent filters, and model attribute sets.
 */
class WorkerCacheTest {

    private WorkerCache workerCache;

    @Mock
    private Agent mockAgent1;

    @Mock
    private Agent mockAgent2;

    @Mock
    private ModelAttributeSet mockModelAttributeSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialise the WorkerCache to store agent copies by default.
        workerCache = new WorkerCache(true);

        // Mock agent behaviours
        when(mockAgent1.name()).thenReturn("Agent1");
        when(mockAgent1.duplicateWithoutRecords()).thenReturn(mockAgent1);

        when(mockAgent2.name()).thenReturn("Agent2");
        when(mockAgent2.duplicateWithoutRecords()).thenReturn(mockAgent2);

        // Mock ModelAttributeSet behaviours
        when(mockModelAttributeSet.name()).thenReturn("AttributeSet1");
    }

    /**
     * Tests adding and retrieving agents.
     */
    @Test
    void testAddAndRetrieveAgents() {
        workerCache.addAgent(mockAgent1);
        workerCache.addAgent(mockAgent2);

        // Verify the agents are retrievable by name.
        assertEquals(mockAgent1, workerCache.getAgent("Agent1"), "Agent1 should be retrievable from the cache.");
        assertEquals(mockAgent2, workerCache.getAgent("Agent2"), "Agent2 should be retrievable from the cache.");

        // Verify the agent existence check.
        assertTrue(workerCache.doesAgentExist("Agent1"), "Agent1 should exist in the cache.");
        assertTrue(workerCache.doesAgentExist("Agent2"), "Agent2 should exist in the cache.");
        assertFalse(workerCache.doesAgentExist("NonExistentAgent"), "Non-existent agent should not exist in the cache.");
    }

    /**
     * Tests adding multiple agents at once.
     */
    @Test
    void testAddMultipleAgents() {
        workerCache.addAgents(List.of(mockAgent1, mockAgent2));

        // Verify both agents are added.
        assertEquals(mockAgent1, workerCache.getAgent("Agent1"), "Agent1 should be retrievable after batch addition.");
        assertEquals(mockAgent2, workerCache.getAgent("Agent2"), "Agent2 should be retrievable after batch addition.");
    }

    /**
     * Tests filtering agents using a predicate.
     */
    @Test
    void testFilterAgents() {
        workerCache.addAgents(List.of(mockAgent1, mockAgent2));

        // Define a filter to retrieve only Agent1.
        Predicate<Agent> filter = agent -> "Agent1".equals(agent.name());
        List<Agent> filteredAgents = workerCache.getFilteredAgents(filter);

        assertEquals(1, filteredAgents.size(), "Only one agent should match the filter.");
        assertEquals(mockAgent1, filteredAgents.get(0), "The filtered agent should be Agent1.");
    }

    /**
     * Tests clearing the cache.
     */
    @Test
    void testClearCache() {
        workerCache.addAgent(mockAgent1);
        workerCache.addAgentFilter(agent -> true);
        workerCache.setModelAttributeSet(mockModelAttributeSet);

        workerCache.clear();

        // Verify all caches are cleared.
        assertNull(workerCache.getAgent("Agent1"), "No agent should be retrievable after clearing the cache.");
        assertFalse(workerCache.doesAgentFilterExist(agent -> true), "Agent filters should be cleared.");
        assertNull(workerCache.getModelAttributeSet("AttributeSet1"), "Model attribute sets should be cleared.");
    }

    /**
     * Tests adding and retrieving agent filters.
     */
    @Test
    void testAddAndRetrieveAgentFilters() {
        Predicate<Agent> filter = agent -> true;

        workerCache.addAgentFilter(filter);

        // Verify the filter exists.
        assertTrue(workerCache.doesAgentFilterExist(filter), "The added filter should exist in the cache.");
    }

    /**
     * Tests adding and retrieving model attribute sets.
     */
    @Test
    void testAddAndRetrieveModelAttributeSets() {
        workerCache.setModelAttributeSet(mockModelAttributeSet);

        // Verify the model attribute set is retrievable.
        assertEquals(mockModelAttributeSet, workerCache.getModelAttributeSet("AttributeSet1"),
                "The model attribute set should be retrievable by name.");

        // Verify the existence check for model attribute sets.
        assertTrue(workerCache.doModelAttributeSetsExist(), "The cache should indicate that model attribute sets exist.");
    }

    /**
     * Tests behaviour with no model attribute sets.
     */
    @Test
    void testNoModelAttributeSets() {
        // Verify no model attribute sets exist initially.
        assertFalse(workerCache.doModelAttributeSetsExist(), "The cache should initially indicate no model attribute sets exist.");
    }
}
