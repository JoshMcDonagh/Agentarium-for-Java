package models.multithreading.requestresponse;

import agents.Agent;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CoordinatorRequestHandler} class and its subclasses.
 * <p>
 * These tests verify the correct handling of various request types and their
 * interactions with shared dependencies, such as the response queue, agent store,
 * and model attribute sets.
 */
class CoordinatorRequestHandlerTest {

    private CoordinatorRequestHandler handler; // Generic handler for testing
    private BlockingQueue<Request> requestQueue; // Queue for incoming requests
    private BlockingQueue<Response> responseQueue; // Queue for outgoing responses

    @Mock
    private AgentStore mockAgentStore; // Mocked AgentStore for dependency injection

    @Mock
    private ModelAttributeSet mockModelAttributeSet; // Mocked ModelAttributeSet for testing

    private Map<String, ModelAttributeSet> modelAttributeSetMap; // Map of model attribute sets

    /**
     * Initialises the required mocks, data structures, and request handlers before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialise request and response queues
        requestQueue = new ArrayBlockingQueue<>(10);
        responseQueue = new ArrayBlockingQueue<>(10);

        // Initialise model attribute set map with a mock
        modelAttributeSetMap = new HashMap<>();
        modelAttributeSetMap.put("attribute1", mockModelAttributeSet);

        // Initialise the CoordinatorRequestHandler with mocks
        CoordinatorRequestHandler.initialise(
                "CoordinatorThread",
                4, // Number of worker threads
                modelAttributeSetMap,
                responseQueue,
                mockAgentStore
        );
    }

    /**
     * Tests the handling of the "All Workers Finish Tick" request.
     * Verifies that responses are sent only after all workers have completed their tick.
     */
    @Test
    void testAllWorkersFinishTick() throws InterruptedException {
        // Simulate the first worker sending the finish tick request
        Request request = new Request("Worker1", null, RequestCodes.ALL_WORKERS_FINISH_TICK, null);
        CoordinatorRequestHandler.handleCoordinatorRequest(request);

        // Verify no response is sent until all workers have finished
        assertTrue(responseQueue.isEmpty(), "Response should not be sent until all workers finish their tick.");

        // Simulate the remaining workers finishing their tick
        for (int i = 2; i <= 4; i++) {
            request = new Request("Worker" + i, null, RequestCodes.ALL_WORKERS_FINISH_TICK, null);
            CoordinatorRequestHandler.handleCoordinatorRequest(request);
        }

        // Verify that responses are sent to all workers
        assertEquals(4, responseQueue.size(), "Responses should be sent to all workers once all have finished their tick.");
    }

    /**
     * Tests the handling of the "Agent Access" request.
     * Verifies that the correct agent is retrieved and sent in the response.
     */
    @Test
    void testAgentAccess() throws InterruptedException {
        // Mock an agent and configure the mock store to return it
        Agent mockAgent = mock(Agent.class);
        when(mockAgentStore.get("Agent1")).thenReturn(mockAgent);

        // Create and handle an AgentAccess request
        Request request = new Request("Requester", "Agent1", RequestCodes.AGENT_ACCESS, "Agent1");
        CoordinatorRequestHandler.handleCoordinatorRequest(request);

        // Verify the response contains the expected agent
        Response response = responseQueue.take();
        assertEquals(ResponseCodes.AGENT_ACCESS, response.getResponseCode(), "Response code should match AGENT_ACCESS.");
        assertEquals(mockAgent, response.getPayload(), "Response payload should match the mock agent.");
    }

    /**
     * Tests the handling of the "Update Coordinator Agents" request.
     * Verifies that the agent store is updated correctly.
     */
    @Test
    void testUpdateCoordinatorAgents() throws InterruptedException {
        // Create a new AgentStore and send an update request
        AgentStore newAgentStore = new AgentStore();
        Request request = new Request(null, null, RequestCodes.UPDATE_COORDINATOR_AGENTS, newAgentStore);

        CoordinatorRequestHandler.handleCoordinatorRequest(request);

        // Verify the mock agent store's update method is called
        verify(mockAgentStore, times(1)).update(newAgentStore);
    }

    /**
     * Tests the handling of the "Filtered Agents Access" request.
     * Verifies that the correct agents matching the filter are returned.
     */
    @Test
    void testFilteredAgentsAccess() throws InterruptedException {
        // Define a predicate filter for testing
        Predicate<Agent> agentFilter = agent -> true;

        // Create and handle a FilteredAgentsAccess request
        Request request = new Request("Requester", null, RequestCodes.FILTERED_AGENTS_ACCESS, agentFilter);
        CoordinatorRequestHandler.handleCoordinatorRequest(request);

        // Verify the response contains the filtered agents
        Response response = responseQueue.take();
        assertEquals(ResponseCodes.FILTERED_AGENTS_ACCESS, response.getResponseCode(), "Response code should match FILTERED_AGENTS_ACCESS.");
        verify(mockAgentStore, times(1)).getFilteredAgents(agentFilter);
    }

    /**
     * Tests the handling of the "Model Attributes Access" request.
     * Verifies that the model attribute set map is returned correctly.
     */
    @Test
    void testModelAttributesAccess() throws InterruptedException {
        // Create and handle a ModelAttributesAccess request
        Request request = new Request("Requester", null, RequestCodes.MODEL_ATTRIBUTES_ACCESS, null);
        CoordinatorRequestHandler.handleCoordinatorRequest(request);

        // Verify the response contains the expected model attribute set map
        Response response = responseQueue.take();
        assertEquals(ResponseCodes.MODEL_ATTRIBUTES_ACCESS, response.getResponseCode(), "Response code should match MODEL_ATTRIBUTES_ACCESS.");
        assertEquals(modelAttributeSetMap, response.getPayload(), "Response payload should match the model attribute set map.");
    }
}
