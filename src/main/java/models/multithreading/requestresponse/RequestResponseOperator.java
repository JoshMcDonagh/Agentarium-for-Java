package models.multithreading.requestresponse;

import agents.Agent;
import models.ModelClock;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

public class RequestResponseOperator {
    private final String threadName;
    private final ModelClock clock;
    private final boolean areProcessesSynced;
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    public RequestResponseOperator(String threadName, ModelClock clock, boolean areProcessesSynced, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.threadName = threadName;
        this.clock = clock;
        this.areProcessesSynced = areProcessesSynced;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    private void wait(String requestCode, String responseCode) throws InterruptedException {
        if (!areProcessesSynced)
            return;

        requestQueue.put(new Request(threadName, null, requestCode, null));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), responseCode) && Objects.equals(response.getDestination(), threadName))
                return;
            else
                responseQueue.put(response);
        }
    }

    public void waitUntilAllWorkersFinishTick() throws InterruptedException {
        wait(RequestCodes.ALL_WORKERS_FINISH_TICK, ResponseCodes.ALL_WORKERS_FINISH_TICK);
    }

    public void waitUntilAllWorkersUpdateCoordinator() throws InterruptedException {
        wait(RequestCodes.UPDATE_COORDINATOR_AGENTS, ResponseCodes.ALL_WORKERS_UPDATE_COORDINATOR);
    }

    public Agent getAgentFromCoordinator(String requesterAgentName, String targetAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, targetAgentName, RequestCodes.AGENT_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), ResponseCodes.AGENT_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName))
                return (Agent) response.getPayload();
            responseQueue.put(response);
        }
    }

    public List<Agent> getFilteredAgentsFromCoordinator(String requesterAgentName, Predicate<Agent> agentFilter) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestCodes.FILTERED_AGENTS_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), ResponseCodes.FILTERED_AGENTS_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName))
                return (List<Agent>) response.getPayload();
            responseQueue.put(response);
        }
    }

    public Map<String, ModelAttributeSet> getModelAttributesFromCoordinator(String requesterAgentName) throws InterruptedException {
        requestQueue.put(new Request(requesterAgentName, null, RequestCodes.MODEL_ATTRIBUTES_ACCESS, null));

        while (true) {
            while (responseQueue.isEmpty());

            Response response = responseQueue.poll();

            if (Objects.equals(response.getResponseCode(), ResponseCodes.MODEL_ATTRIBUTES_ACCESS) && Objects.equals(response.getDestination(), requesterAgentName))
                return (Map<String, ModelAttributeSet>) response.getPayload();
            responseQueue.put(response);
        }
    }

    public void updateCoordinatorAgents(AgentStore agentStore) throws InterruptedException {
        requestQueue.put(new Request(null, null, RequestCodes.UPDATE_COORDINATOR_AGENTS, agentStore));
    }
}
