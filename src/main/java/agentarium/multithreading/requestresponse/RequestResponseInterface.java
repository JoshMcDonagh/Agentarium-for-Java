package agentarium.multithreading.requestresponse;

import agentarium.ModelSettings;
import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

/**
 * Provides a per-thread interface for sending requests to and receiving responses from
 * the simulation coordinator in a synchronised agent-based model.
 *
 * <p>This abstraction helps worker threads interact with the coordinator using
 * higher-level methods instead of directly manipulating queues.
 */
public class RequestResponseInterface {

    /** The name of the current thread or model element using this interface */
    private final String name;

    /** Whether the simulation is running in synchronised (coordinated) mode */
    private final boolean areProcessesSynced;

    /** Shared queue for outgoing requests */
    private final BlockingQueue<Request> requestQueue;

    /** Shared queue for incoming responses */
    private final BlockingQueue<Response> responseQueue;

    /**
     * Constructs a new interface bound to the given model element or thread.
     *
     * @param name the name of this component (typically the thread or agent name)
     * @param settings the shared model settings
     * @param requestResponseController the controller that provides the queues
     */
    public RequestResponseInterface(String name, ModelSettings settings, RequestResponseController requestResponseController) {
        this.name = name;
        this.areProcessesSynced = settings.getAreProcessesSynced();
        this.requestQueue = requestResponseController.getRequestQueue();
        this.responseQueue = requestResponseController.getResponseQueue();
    }

    /**
     * Waits for a specific response type after placing a corresponding request.
     * Used for synchronisation barriers between threads.
     */
    private void wait(RequestType requestType, ResponseType responseType) throws InterruptedException {
        if (!areProcessesSynced)
            return;

        requestQueue.put(new Request(name, null, requestType, null));

        while (true) {
            while (responseQueue.isEmpty()); // Busy-wait until a response is available
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), responseType)
                    && Objects.equals(response.getDestination(), name)) {
                return;
            } else {
                // Put unrelated responses back on the queue
                responseQueue.put(response);
            }
        }
    }

    /**
     * Waits until all workers have completed their current simulation tick.
     */
    public void waitUntilAllWorkersFinishTick() throws InterruptedException {
        wait(RequestType.ALL_WORKERS_FINISH_TICK, ResponseType.ALL_WORKERS_FINISH_TICK);
    }

    /**
     * Waits until all workers have updated the coordinator with their agent data.
     */
    public void waitUntilAllWorkersUpdateCoordinator() throws InterruptedException {
        wait(RequestType.UPDATE_COORDINATOR_AGENTS, ResponseType.ALL_WORKERS_UPDATE_COORDINATOR);
    }

    /**
     * Requests a specific agent from the coordinator.
     *
     * @param requesterAgentName the name of the requesting agent
     * @param targetAgentName the name of the agent to retrieve
     * @return the {@link Agent} instance returned by the coordinator
     */
    public Agent getAgentFromCoordinator(String requesterAgentName, String targetAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, targetAgentName, RequestType.AGENT_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.AGENT_ACCESS)
                    && Objects.equals(response.getDestination(), requesterAgentName))
                return (Agent) response.getPayload();
            responseQueue.put(response);
        }
    }

    /**
     * Requests a filtered subset of agents from the coordinator.
     *
     * @param requesterAgentName the name of the requester
     * @param agentFilter a predicate to apply to the global agent set
     * @return an {@link AgentSet} containing matching agents
     */
    public AgentSet getFilteredAgentsFromCoordinator(String requesterAgentName, Predicate<Agent> agentFilter) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestType.FILTERED_AGENTS_ACCESS, agentFilter));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.FILTERED_AGENTS_ACCESS)
                    && Objects.equals(response.getDestination(), requesterAgentName))
                return (AgentSet) response.getPayload();
            responseQueue.put(response);
        }
    }

    /**
     * Requests the current environment state from the coordinator.
     *
     * @param requesterAgentName the requesting agent's name
     * @return the current {@link Environment} instance
     */
    public Environment getEnvironmentFromCoordinator(String requesterAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestType.ENVIRONMENT_ATTRIBUTES_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.ENVIRONMENT_ATTRIBUTES_ACCESS)
                    && Objects.equals(response.getDestination(), name))
                return (Environment) response.getPayload();
            responseQueue.put(response);
        }
    }

    /**
     * Sends an update to the coordinator with the current agent set for this thread.
     *
     * @param agentSet the updated set of agents
     */
    public void updateCoordinatorAgents(AgentSet agentSet) throws InterruptedException {
        requestQueue.put(new Request(null, null, RequestType.UPDATE_COORDINATOR_AGENTS, agentSet));
    }
}
