package agentarium.multithreading.requestresponse;

import agentarium.ModelSettings;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public abstract class CoordinatorRequestHandler {
    private static Map<String, CoordinatorRequestHandler> requestHandlerMap;

    public static void initialise(String threadName,
                                  ModelSettings settings,
                                  BlockingQueue<Response> responseQueue,
                                  AgentSet globalAgentSet,
                                  Environment environment) {
        requestHandlerMap = new HashMap<>();
        requestHandlerMap.put(RequestType.ALL_WORKERS_FINISH_TICK,
                new AllWorkersFinishTick(threadName, settings.getNumOfCores(), responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.ALL_WORKERS_UPDATE_COORDINATOR,
                new AllWorkersUpdateCoordinator(threadName, settings.getNumOfCores(), responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.AGENT_ACCESS,
                new AgentAccess(threadName, settings.getNumOfCores(), responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.UPDATE_COORDINATOR_AGENTS,
                new UpdateCoordinatorAgents(threadName, settings.getNumOfCores(), responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.FILTERED_AGENTS_ACCESS,
                new FilteredAgentsAccess(threadName, settings.getNumOfCores(), responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.ENVIRONMENT_ATTRIBUTES_ACCESS,
                new ModelAttributesAccess(threadName, settings.getNumOfCores(), responseQueue, globalAgentSet, environment));
    }

    public static void handleCoordinatorRequest(Request request) throws InterruptedException {
        CoordinatorRequestHandler handler = requestHandlerMap.get(request.getRequestType());
        if (handler != null)
            handler.handleRequest(request);
    }

    private final String threadName;
    private final ModelSettings settings;
    private final BlockingQueue<Response> responseQueue;
    private final AgentSet globalAgentSet;
    private final Environment environment;
    private List<String> workersWaiting = new ArrayList<>();

    public CoordinatorRequestHandler(String threadName,
                                     ModelSettings settings,
                                     BlockingQueue<Response> responseQueue,
                                     AgentSet globalAgentSet,
                                     Environment environment) {
        this.threadName = threadName;
        this.settings = settings;
        this.responseQueue = responseQueue;
        this.globalAgentSet = globalAgentSet;
        this.environment = environment;
    }

    protected String getThreadName() {
        return threadName;
    }

    protected ModelSettings getSettings() {
        return settings;
    }

    protected BlockingQueue<Response> getResponseQueue() {
        return responseQueue;
    }

    protected List<String> getWorkersWaiting() {
        return workersWaiting;
    }

    protected void setWorkersWaiting(List<String> workersWaiting) {
        this.workersWaiting = workersWaiting;
    }

    public abstract void handleRequest(Request request) throws InterruptedException;

    
}
