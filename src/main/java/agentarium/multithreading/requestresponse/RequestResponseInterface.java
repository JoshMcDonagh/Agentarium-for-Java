package agentarium.multithreading.requestresponse;

import agentarium.ModelSettings;
import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.AttributeSet;
import agentarium.environments.Environment;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

public class RequestResponseInterface {
    private final String name;
    private final boolean areProcessesSynced;
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    public RequestResponseInterface(String name, ModelSettings settings, RequestResponseController requestResponseController) {
        this.name = name;
        this.areProcessesSynced = settings.getAreProcessesSynced();
        this.requestQueue = requestResponseController.getRequestQueue();
        this.responseQueue = requestResponseController.getResponseQueue();
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

    public Agent getAgentFromCoordinator(String requesterAgentName, String targetAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, targetAgentName, RequestType.AGENT_ACCESS, null));

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

    public Environment getEnvironmentFromCoordinator(String requesterAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestType.ENVIRONMENT_ATTRIBUTES_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());
            Response response = responseQueue.poll();
            if (Objects.equals(response.getResponseType(), ResponseType.ENVIRONMENT_ATTRIBUTES_ACCESS) && Objects.equals(response.getDestination(), name))
                return (Environment) response.getPayload();
            responseQueue.put(response);
        }
    }

    public void updateCoordinatorAgents(AgentSet agentSet) throws InterruptedException {
        requestQueue.put(new Request(null, null, RequestType.UPDATE_COORDINATOR_AGENTS, agentSet));
    }
}
