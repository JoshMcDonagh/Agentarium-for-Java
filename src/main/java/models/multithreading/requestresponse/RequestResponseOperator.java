package models.multithreading.requestresponse;

import agents.Agent;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

/**
 * The `RequestResponseOperator` class facilitates communication between threads
 * in a multi-threaded simulation environment. It handles request and response
 * operations to enable synchronised processes between workers and coordinators.
 */
public class RequestResponseOperator {

    // Name of the thread using this operator.
    private final String threadName;

    // Flag indicating whether processes are synchronised.
    private final boolean areProcessesSynced;

    // Queue for sending requests.
    private final BlockingQueue<Request> requestQueue;

    // Queue for receiving responses.
    private final BlockingQueue<Response> responseQueue;

    /**
     * Constructs a `RequestResponseOperator` to manage communication for a thread.
     *
     * @param threadName        The name of the thread using this operator.
     * @param areProcessesSynced Whether processes should synchronise with requests and responses.
     * @param requestQueue      The queue for sending requests.
     * @param responseQueue     The queue for receiving responses.
     */
    public RequestResponseOperator(String threadName, boolean areProcessesSynced, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.threadName = threadName;
        this.areProcessesSynced = areProcessesSynced;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    /**
     * Waits for a specific response based on a request and response code.
     *
     * @param requestCode  The code of the request to send.
     * @param responseCode The expected response code to wait for.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    private void wait(String requestCode, String responseCode) throws InterruptedException {
        if (!areProcessesSynced) {
            return;
        }

        // Send a request.
        requestQueue.put(new Request(threadName, null, requestCode, null));

        while (true) {
            // Wait for a response.
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            // Check if the response matches the expected response code and destination.
            if (Objects.equals(response.getResponseCode(), responseCode) && Objects.equals(response.getDestination(), threadName)) {
                return;
            } else {
                // Requeue unmatched responses.
                responseQueue.put(response);
            }
        }
    }

    /**
     * Waits until all workers finish their current tick.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void waitUntilAllWorkersFinishTick() throws InterruptedException {
        wait(RequestCodes.ALL_WORKERS_FINISH_TICK, ResponseCodes.ALL_WORKERS_FINISH_TICK);
    }

    /**
     * Waits until all workers update the coordinator.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void waitUntilAllWorkersUpdateCoordinator() throws InterruptedException {
        wait(RequestCodes.UPDATE_COORDINATOR_AGENTS, ResponseCodes.ALL_WORKERS_UPDATE_COORDINATOR);
    }

    /**
     * Retrieves a specific agent from the coordinator.
     *
     * @param requesterAgentName The name of the requesting agent.
     * @param targetAgentName    The name of the target agent to retrieve.
     * @return The requested agent.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public Agent getAgentFromCoordinator(String requesterAgentName, String targetAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, targetAgentName, RequestCodes.AGENT_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), ResponseCodes.AGENT_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName)) {
                return (Agent) response.getPayload();
            }
            responseQueue.put(response);
        }
    }

    /**
     * Retrieves a list of filtered agents from the coordinator.
     *
     * @param requesterAgentName The name of the requesting agent.
     * @param agentFilter        The filter to apply to agents.
     * @return A list of agents matching the filter.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public List<Agent> getFilteredAgentsFromCoordinator(String requesterAgentName, Predicate<Agent> agentFilter) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestCodes.FILTERED_AGENTS_ACCESS, agentFilter));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), ResponseCodes.FILTERED_AGENTS_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName)) {
                return (List<Agent>) response.getPayload();
            }
            responseQueue.put(response);
        }
    }

    /**
     * Retrieves model attributes from the coordinator.
     *
     * @param requesterAgentName The name of the requesting agent.
     * @return A map of model attributes.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public Map<String, ModelAttributeSet> getModelAttributesFromCoordinator(String requesterAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestCodes.MODEL_ATTRIBUTES_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), ResponseCodes.MODEL_ATTRIBUTES_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName)) {
                return (Map<String, ModelAttributeSet>) response.getPayload();
            }
            responseQueue.put(response);
        }
    }

    /**
     * Sends a request to update the coordinator with new agents.
     *
     * @param agentStore The store of agents to update in the coordinator.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void updateCoordinatorAgents(AgentStore agentStore) throws InterruptedException {
        requestQueue.put(new Request(null, null, RequestCodes.UPDATE_COORDINATOR_AGENTS, agentStore));
    }
}
