package agentarium.multithreading;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.AttributeSet;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

public class RequestResponseInterface {
    private final String name;
    private final boolean areProcessesSynced;
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    RequestResponseInterface(String name, boolean areProcessesSynced, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.name = name;
        this.areProcessesSynced = areProcessesSynced;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    private void wait(RequestType requestType, ResponseType responseType) throws InterruptedException {
        if (!areProcessesSynced)
            return;

        requestQueue.put(new Request(name, null, requestType, null));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), responseType) && Objects.equals(response.getDestination(), name))
                return;
            else
                responseQueue.put(response);
        }
    }

    public void waitUntilAllWorkersFinishTick() throws InterruptedException {
        wait(RequestType.ALL_WORKERS_FINISH_TICK, ResponseType.ALL_WORKERS_FINISH_TICK);
    }

    public void waitUntilAllWorkersUpdateCoordinator() throws InterruptedException {
        wait(RequestType.UPDATE_COORDINATOR_AGENTS, ResponseType.ALL_WORKERS_UPDATE_COORDINATOR);
    }

    public Agent getAgentFromCoordinator(String requesterAgentName, Predicate<Agent> agentFilter) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestType.AGENT_ACCESS, agentFilter));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.AGENT_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName))
                return (Agent) response.getPayload();
            responseQueue.put(response);
        }
    }

    public AgentSet getFilteredAgentsFromCoordinator(String requesterAgentName, Predicate<Agent> agentFilter) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestType.FILTERED_AGENTS_ACCESS, agentFilter));

        while(true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.FILTERED_AGENTS_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName))
                return (AgentSet) response.getPayload();
            responseQueue.put(response);
        }
    }

    public Map<String, AttributeSet> getEnvironmentAttributesFromCoordinator(String requesterAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestType.ENVIRONMENT_ATTRIBUTES_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.ENVIRONMENT_ATTRIBUTES_ACCESS) && Objects.equals(response.getDestination(), name))
                return (Map<String, AttributeSet>) response.getPayload();
            responseQueue.put(response);
        }
    }

    public void updateCoordinatorAgents(AgentSet agentSet) throws InterruptedException {
        requestQueue.put(new Request(null, null, RequestType.UPDATE_COORDINATOR_AGENTS, agentSet));
    }
}
