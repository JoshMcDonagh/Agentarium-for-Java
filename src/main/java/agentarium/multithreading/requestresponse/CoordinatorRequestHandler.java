package agentarium.multithreading.requestresponse;

import agentarium.ModelSettings;
import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

/**
 * Abstract base class for handling requests sent to the coordinator thread in a synchronised model.
 *
 * <p>Each request type is mapped to a specific implementation of this handler. The {@link #initialise}
 * method sets up this mapping, and {@link #handleCoordinatorRequest(Request)} dispatches requests accordingly.
 *
 * <p>All handler subclasses must implement {@link #handleRequest(Request)}.
 */
public abstract class CoordinatorRequestHandler {

    /** Static mapping from request type to its associated handler */
    private static Map<RequestType, CoordinatorRequestHandler> requestHandlerMap;

    /**
     * Initialises the handler map for the coordinator, assigning an instance of each
     * request type's handler.
     *
     * @param threadName the coordinator thread's name
     * @param settings the global model settings
     * @param responseQueue the shared response queue
     * @param globalAgentSet the global agent set known to the coordinator
     * @param environment the shared environment
     */
    public static void initialise(String threadName,
                                  ModelSettings settings,
                                  BlockingQueue<Response> responseQueue,
                                  AgentSet globalAgentSet,
                                  Environment environment) {
        requestHandlerMap = new HashMap<>();
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

    /**
     * Handles a coordinator request by dispatching it to the appropriate handler.
     *
     * @param request the incoming request from a worker
     */
    public static void handleCoordinatorRequest(Request request) throws InterruptedException {
        CoordinatorRequestHandler handler = requestHandlerMap.get(request.getRequestType());
        if (handler != null)
            handler.handleRequest(request);
    }

    // Instance fields common to all handlers
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

    /** @return the coordinator thread name */
    protected String getThreadName() {
        return threadName;
    }

    /** @return the global model settings */
    protected ModelSettings getSettings() {
        return settings;
    }

    /** @return the queue to which coordinator responses are written */
    protected BlockingQueue<Response> getResponseQueue() {
        return responseQueue;
    }

    /** @return the current global set of all agents */
    protected AgentSet getGlobalAgentSet() {
        return globalAgentSet;
    }

    /** @return the global environment */
    protected Environment getEnvironment() {
        return environment;
    }

    /** @return the list of workers currently waiting for a synchronisation barrier */
    protected List<String> getWorkersWaiting() {
        return workersWaiting;
    }

    /** Replaces the list of waiting workers (typically to reset it) */
    protected void setWorkersWaiting(List<String> workersWaiting) {
        this.workersWaiting = workersWaiting;
    }

    /**
     * Handles an incoming request from a worker. Must be implemented by subclasses.
     *
     * @param request the request to handle
     */
    public abstract void handleRequest(Request request) throws InterruptedException;

    // === Specific request handler implementations ===

    /**
     * Handles synchronisation for when all workers finish a tick.
     */
    public static class AllWorkersFinishTick extends CoordinatorRequestHandler {
        public AllWorkersFinishTick(String threadName, ModelSettings settings, BlockingQueue<Response> responseQueue, AgentSet globalAgentSet, Environment environment) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());
            if (getWorkersWaiting().size() == getSettings().getNumOfCores()) {
                for (String worker : getWorkersWaiting()) {
                    getResponseQueue().put(new Response(getThreadName(), worker, ResponseType.ALL_WORKERS_FINISH_TICK, null));
                }
                setWorkersWaiting(new ArrayList<>());
            }
        }
    }

    /**
     * Handles synchronisation for when all workers have updated the coordinator.
     */
    public static class AllWorkersUpdateCoordinator extends CoordinatorRequestHandler {
        public AllWorkersUpdateCoordinator(String threadName, ModelSettings settings, BlockingQueue<Response> responseQueue, AgentSet globalAgentSet, Environment environment) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());
            if (getWorkersWaiting().size() == getSettings().getNumOfCores()) {
                for (String worker : getWorkersWaiting()) {
                    getResponseQueue().put(new Response(getThreadName(), worker, ResponseType.ALL_WORKERS_UPDATE_COORDINATOR, null));
                }
                setWorkersWaiting(new ArrayList<>());

                // Run the environment after all workers are synchronised
                getEnvironment().run();
            }
        }
    }

    /**
     * Provides access to an individual agent by name.
     */
    public static class AgentAccess extends CoordinatorRequestHandler {
        public AgentAccess(String threadName, ModelSettings settings, BlockingQueue<Response> responseQueue, AgentSet globalAgentSet, Environment environment) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            Agent agent = getGlobalAgentSet().get((String) request.getPayload());
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseType.AGENT_ACCESS, agent));
        }
    }

    /**
     * Updates the global agent set with new agent states received from workers.
     */
    public static class UpdateCoordinatorAgents extends CoordinatorRequestHandler {
        public UpdateCoordinatorAgents(String threadName, ModelSettings settings, BlockingQueue<Response> responseQueue, AgentSet globalAgentSet, Environment environment) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) {
            getGlobalAgentSet().update((AgentSet) request.getPayload());
        }
    }

    /**
     * Provides access to a filtered subset of the global agent set.
     */
    public static class FilteredAgentsAccess extends CoordinatorRequestHandler {
        public FilteredAgentsAccess(String threadName, ModelSettings settings, BlockingQueue<Response> responseQueue, AgentSet globalAgentSet, Environment environment) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            Predicate<Agent> filter = (Predicate<Agent>) request.getPayload();
            AgentSet filtered = getGlobalAgentSet().getFilteredAgents(filter);
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseType.FILTERED_AGENTS_ACCESS, filtered));
        }
    }

    /**
     * Provides access to the current environment state.
     */
    public static class EnvironmentAttributesAccess extends CoordinatorRequestHandler {
        public EnvironmentAttributesAccess(String threadName, ModelSettings settings, BlockingQueue<Response> responseQueue, AgentSet globalAgentSet, Environment environment) {
            super(threadName, settings, responseQueue, globalAgentSet, environment);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseType.ENVIRONMENT_ATTRIBUTES_ACCESS, getEnvironment()));
        }
    }
}
