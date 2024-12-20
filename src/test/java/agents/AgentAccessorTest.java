package agents;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.requestresponse.RequestResponseOperator;
import models.multithreading.threadutilities.AgentStore;
import models.multithreading.threadutilities.WorkerCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AgentAccessor} class.
 * This test suite verifies the behaviour of the AgentAccessor in accessing agents and attributes
 * from the local store, cache, or via a coordinator.
 */
class AgentAccessorTest {

    private AgentAccessor agentAccessor;

    @Mock
    private Agent mockAgent;

    @Mock
    private ModelAttributeSet mockModelAttributeSet;

    @Mock
    private RequestResponseOperator mockRequestResponseOperator;

    @Mock
    private AgentStore mockAgentStore;

    @Mock
    private WorkerCache mockCache;

    /**
     * Set up the test environment before each test case.
     * Mocks the dependencies required by AgentAccessor and stubs their behaviour.
     */
    @BeforeEach
    void setUp() throws InterruptedException {
        MockitoAnnotations.openMocks(this);

        // Mock Agent setup
        when(mockAgent.name()).thenReturn("MockAgent");
        when(mockAgentStore.doesAgentExist("MockAgent")).thenReturn(false);
        when(mockCache.doesAgentExist("MockAgent")).thenReturn(false);

        // Initialise the AgentAccessor with mocked dependencies
        Map<String, ModelAttributeSet> modelAttributeSetMap = Map.of("MockAttribute", mockModelAttributeSet);

        // Mock RequestResponseOperator
        when(mockRequestResponseOperator.getAgentFromCoordinator(anyString(), eq("MockAgent")))
                .thenReturn(mockAgent);

        when(mockRequestResponseOperator.getFilteredAgentsFromCoordinator(anyString(), any()))
                .thenReturn(List.of(mockAgent));

        when(mockRequestResponseOperator.getModelAttributesFromCoordinator(anyString()))
                .thenReturn(Map.of("MockAttribute", mockModelAttributeSet));

        agentAccessor = new AgentAccessor(
                mockAgent,
                List.of(mockModelAttributeSet),
                modelAttributeSetMap,
                mockRequestResponseOperator,
                mockAgentStore,
                true, // areProcessesSynced
                true, // isCacheUsed
                mockCache
        );
    }

    /**
     * Tests that the `doesAgentExistInThisCore` method correctly checks the local store for an agent.
     */
    @Test
    void testDoesAgentExistInThisCore() {
        when(mockAgentStore.doesAgentExist("MockAgent")).thenReturn(true);

        boolean result = agentAccessor.doesAgentExistInThisCore("MockAgent");
        assertTrue(result);
        verify(mockAgentStore).doesAgentExist("MockAgent");
    }

    /**
     * Tests retrieving an agent by name from the local store.
     */
    @Test
    void testGetAgentByNameFromLocalStore() {
        when(mockAgentStore.doesAgentExist("MockAgent")).thenReturn(true);
        when(mockAgentStore.get("MockAgent")).thenReturn(mockAgent);

        Agent result = agentAccessor.getAgentByName("MockAgent");
        assertEquals(mockAgent, result);
        verify(mockAgentStore).get("MockAgent");
    }

    /**
     * Tests retrieving an agent by name from the cache.
     */
    @Test
    void testGetAgentByNameFromCache() {
        when(mockAgentStore.doesAgentExist("MockAgent")).thenReturn(false);
        when(mockCache.doesAgentExist("MockAgent")).thenReturn(true);
        when(mockCache.getAgent("MockAgent")).thenReturn(mockAgent);

        Agent result = agentAccessor.getAgentByName("MockAgent");
        assertEquals(mockAgent, result);
        verify(mockCache).getAgent("MockAgent");
    }

    /**
     * Tests retrieving an agent by name from the coordinator.
     */
    @Test
    void testGetAgentByNameFromCoordinator() throws Exception {
        when(mockAgentStore.doesAgentExist("MockAgent")).thenReturn(false);
        when(mockCache.doesAgentExist("MockAgent")).thenReturn(false);
        when(mockRequestResponseOperator.getAgentFromCoordinator("MockAgent", "MockTargetAgent")).thenReturn(mockAgent);

        Agent result = agentAccessor.getAgentByName("MockTargetAgent");
        assertEquals(mockAgent, result);
        verify(mockRequestResponseOperator).getAgentFromCoordinator("MockAgent", "MockTargetAgent");
    }

    /**
     * Tests filtering agents from the local store based on a predicate.
     */
    @Test
    void testGetFilteredAgentsFromLocalStore() {
        Predicate<Agent> mockFilter = agent -> agent.name().equals("MockAgent");
        when(mockCache.doesAgentFilterExist(mockFilter)).thenReturn(false);
        when(mockAgentStore.getFilteredAgents(mockFilter)).thenReturn(List.of(mockAgent));

        List<Agent> result = agentAccessor.getFilteredAgents(mockFilter);
        assertEquals(1, result.size());
        assertEquals(mockAgent, result.get(0));
    }

    /**
     * Tests retrieving a model attribute set by name from the local store.
     */
    @Test
    void testGetModelAttributeSetFromLocalStore() {
        ModelAttributeSet result = agentAccessor.getModelAttributeSet("MockAttribute");
        assertEquals(mockModelAttributeSet, result);
    }

    /**
     * Tests retrieving a model attribute set by name from the coordinator.
     */
    @Test
    void testGetModelAttributeSetFromCoordinator() throws Exception {
        when(mockCache.doModelAttributeSetsExist()).thenReturn(false);
        when(mockRequestResponseOperator.getModelAttributesFromCoordinator("MockAgent"))
                .thenReturn(Map.of("MockAttribute", mockModelAttributeSet));

        ModelAttributeSet result = agentAccessor.getModelAttributeSet("MockAttribute");
        assertEquals(mockModelAttributeSet, result);
        verify(mockRequestResponseOperator).getModelAttributesFromCoordinator("MockAgent");
    }
}
