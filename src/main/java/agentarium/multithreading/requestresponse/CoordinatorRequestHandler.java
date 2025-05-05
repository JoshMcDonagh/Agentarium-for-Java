package agentarium.multithreading.requestresponse;

import agentarium.ModelSettings;
import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

public abstract class CoordinatorRequestHandler {
    private static Map<RequestType, CoordinatorRequestHandler> requestHandlerMap;

    public static void initialise(String threadName,
                                  ModelSettings settings,
                                  BlockingQueue<Response> responseQueue,
                                  AgentSet globalAgentSet,
                                  Environment environment) {
        requestHandlerMap = new HashMap<RequestType, CoordinatorRequestHandler>();
        requestHandlerMap.put(RequestType.ALL_WORKERS_FINISH_TICK,
                new AllWorkersFinishTick(threadName, settings, responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.ALL_WORKERS_UPDATE_COORDINATOR,
                new AllWorkersUpdateCoordinator(threadName, settings, responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.AGENT_ACCESS,
                new AgentAccess(threadName, settings, responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.UPDATE_COORDINATOR_AGENTS,
                new UpdateCoordinatorAgents(threadName, settings, responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.FILTERED_AGENTS_ACCESS,
                new FilteredAgentsAccess(threadName, settings, responseQueue, globalAgentSet, environment));
        requestHandlerMap.put(RequestType.ENVIRONMENT_ATTRIBUTES_ACCESS,
                new EnvironmentAttributesAccess(threadName, settings, responseQueue, globalAgentSet, environment));
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

    protected AgentSet getGlobalAgentSet() {
        return globalAgentSet;
    }

    protected Environment getEnvironment() {
        return environment;
    }

    protected List<String> getWorkersWaiting() {
        return workersWaiting;
    }

    protected void setWorkersWaiting(List<String> workersWaiting) {
        this.workersWaiting = workersWaiting;
    }

    public abstract void handleRequest(Request request) throws InterruptedException;

    public static class AllWorkersFinishTick extends CoordinatorRequestHandler {
        public AllWorkersFinishTick(
                String threadName,
                ModelSettings settings,
                BlockingQueue<Response> responseQueue,
                AgentSet globalAgentSet,
                Environment environment
        ) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());
            if (getWorkersWaiting().size() == getSettings().getNumOfCores()) {
                for (String worker : getWorkersWaiting()) {
                    getResponseQueue().put(new Response(
                            getThreadName(),
                            worker,
                            ResponseType.ALL_WORKERS_FINISH_TICK,
                            null));
                }
                setWorkersWaiting(new ArrayList<>());
            }
        }
    }

    public static class AllWorkersUpdateCoordinator extends CoordinatorRequestHandler {
        public AllWorkersUpdateCoordinator(
                String threadName,
                ModelSettings settings,
                BlockingQueue<Response> responseQueue,
                AgentSet globalAgentSet,
                Environment environment
        ) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());
            if (getWorkersWaiting().size() == getSettings().getNumOfCores()) {
                for (String worker : getWorkersWaiting())
                    getResponseQueue().put(new Response(
                            getThreadName(),
                            worker,
                            ResponseType.ALL_WORKERS_UPDATE_COORDINATOR,
                            null));
                setWorkersWaiting(new ArrayList<>());

                getEnvironment().run();
            }
        }
    }

    public static class AgentAccess extends CoordinatorRequestHandler {
        public AgentAccess(
                String threadName,
                ModelSettings settings,
                BlockingQueue<Response> responseQueue,
                AgentSet globalAgentSet,
                Environment environment
        ) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(
                    getThreadName(),
                    request.getRequester(),
                    ResponseType.AGENT_ACCESS,
                    getGlobalAgentSet().get((String) request.getPayload())));
        }
    }

    public static class UpdateCoordinatorAgents extends CoordinatorRequestHandler {
        public UpdateCoordinatorAgents(
                String threadName,
                ModelSettings settings,
                BlockingQueue<Response> responseQueue,
                AgentSet globalAgentSet,
                Environment environment
        ) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getGlobalAgentSet().update((AgentSet) request.getPayload());
        }
    }

    public static class FilteredAgentsAccess extends CoordinatorRequestHandler {
        public FilteredAgentsAccess(
                String threadName,
                ModelSettings settings,
                BlockingQueue<Response> responseQueue,
                AgentSet globalAgentSet,
                Environment environment
        ) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(
                    getThreadName(),
                    request.getRequester(),
                    ResponseType.FILTERED_AGENTS_ACCESS,
                    getGlobalAgentSet().getFilteredAgents((Predicate<Agent>) request.getPayload())));
        }
    }

    public static class EnvironmentAttributesAccess extends CoordinatorRequestHandler {
        public EnvironmentAttributesAccess(
                String threadName,
                ModelSettings settings,
                BlockingQueue<Response> responseQueue,
                AgentSet globalAgentSet,
                Environment environment
        ) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(
                    getThreadName(),
                    request.getRequester(),
                    ResponseType.ENVIRONMENT_ATTRIBUTES_ACCESS,
                    getEnvironment()
            ));
        }
    }
}
