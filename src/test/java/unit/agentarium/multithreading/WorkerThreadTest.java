package unit.agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.multithreading.WorkerThread;
import agentarium.multithreading.requestresponse.RequestResponseController;
import agentarium.multithreading.requestresponse.RequestResponseInterface;
import agentarium.results.Results;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link WorkerThread} class.
 *
 * <p>Verifies correct behaviour of the simulation loop in controlled conditions using mocks.
 */
public class WorkerThreadTest {

    private ModelSettings settings;
    private AgentSet agents;
    private AgentSet duplicatedAgents;
    private RequestResponseController controller;
    private RequestResponseInterface requestInterface;

    @BeforeEach
    void setUp() throws Exception {
        settings = mock(ModelSettings.class);
        agents = mock(AgentSet.class);
        duplicatedAgents = mock(AgentSet.class);
        controller = mock(RequestResponseController.class);
        requestInterface = mock(RequestResponseInterface.class);

        when(settings.getIsCacheUsed()).thenReturn(false);
        when(settings.getAreProcessesSynced()).thenReturn(false);
        when(settings.getTotalNumOfTicks()).thenReturn(2);  // keep it small for test speed
        when(settings.getModelScheduler()).thenReturn(tick -> {}); // no-op scheduler
        when(settings.getResults()).thenReturn(new DummyResults());
        when(agents.duplicate()).thenReturn(duplicatedAgents);
        when(controller.getInterface(any())).thenReturn(requestInterface);

        Agent mockAgent = mock(Agent.class);
        when(mockAgent.getName()).thenReturn("Agent1");
        when(agents.iterator()).thenReturn(List.of(mockAgent).iterator());
    }

    @Test
    void testWorkerThreadCallReturnsValidResults() throws Exception {
        WorkerThread<Results> worker = new WorkerThread<>("Worker-Test", settings, controller, agents);
        Results results = worker.call();

        assertNotNull(results, "Worker should return non-null results.");
        assertTrue(results instanceof DummyResults, "Results should be of type DummyResults.");
    }

    /**
     * A concrete implementation of {@link Results} for test purposes.
     * The accumulation methods return empty lists to satisfy abstract requirements.
     */
    public static class DummyResults extends Results {
        @Override
        protected List<?> accumulateAgentPropertyResults(String attributeSetName, String propertyName,
                                                         List<?> accumulatedValues, List<?> valuesToBeProcessed) {
            return List.of(); // Minimal implementation for testing
        }

        @Override
        protected List<?> accumulateAgentPreEventResults(String attributeSetName, String preEventName,
                                                         List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
            return List.of();
        }

        @Override
        protected List<?> accumulateAgentPostEventResults(String attributeSetName, String postEventName,
                                                          List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
            return List.of();
        }
    }
}
