package models.multithreading.requestresponse;

import agents.Agent;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

/**
 * The `CoordinatorRequestHandler` is an abstract class responsible for processing
 * and managing requests received by the Coordinator from Workers.
 * <p>
 * It provides specialised handlers for different request types and facilitates communication
 * between worker threads and the coordinator thread.
 */
public abstract class CoordinatorRequestHandler {

    // Map linking request codes to their corresponding handler instances.
    private static Map<String, CoordinatorRequestHandler> requestHandlersMap;

    /**
     * Initialises the request handlers with the necessary configurations for processing requests.
     *
     * @param threadName          The name of the coordinator thread.
     * @param numOfCores          The number of worker threads.
     * @param modelAttributeSetMap A map of model attribute sets managed by the coordinator.
     * @param responseQueue       A shared queue for sending responses.
     * @param globalAgentStore    A shared store for managing agents.
     */
    public static void initialise(
            String threadName,
            int numOfCores,
            Map<String, ModelAttributeSet> modelAttributeSetMap,
            BlockingQueue<Response> responseQueue,
            AgentStore globalAgentStore
    ) {
        requestHandlersMap = new HashMap<>();
        requestHandlersMap.put(RequestCodes.ALL_WORKERS_FINISH_TICK,
                new AllWorkersFinishTick(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore));
        requestHandlersMap.put(RequestCodes.ALL_WORKERS_UPDATE_COORDINATOR,
                new AllWorkersUpdateCoordinator(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore));
        requestHandlersMap.put(RequestCodes.AGENT_ACCESS,
                new AgentAccess(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore));
        requestHandlersMap.put(RequestCodes.UPDATE_COORDINATOR_AGENTS,
                new UpdateCoordinatorAgents(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore));
        requestHandlersMap.put(RequestCodes.FILTERED_AGENTS_ACCESS,
                new FilteredAgentsAccess(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore));
        requestHandlersMap.put(RequestCodes.MODEL_ATTRIBUTES_ACCESS,
                new ModelAttributesAccess(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore));
    }

    /**
     * Handles a coordinator request by delegating to the appropriate handler.
     *
     * @param request The request to process.
     * @throws InterruptedException If interrupted while processing the request.
     */
    public static void handleCoordinatorRequest(Request request) throws InterruptedException {
        CoordinatorRequestHandler handler = requestHandlersMap.get(request.getRequestCode());
        if (handler != null) {
            handler.handleRequest(request);
        }
    }

    // Instance-specific attributes
    private final String threadName;
    private final int numOfCores;
    private final Map<String, ModelAttributeSet> modelAttributeSetMap;
    private final BlockingQueue<Response> responseQueue;
    private final AgentStore globalAgentStore;
    private List<String> workersWaiting = new ArrayList<>();

    /**
     * Constructs a `CoordinatorRequestHandler` with the specified parameters.
     *
     * @param threadName          The name of the coordinator thread.
     * @param numOfCores          The number of worker threads.
     * @param modelAttributeSetMap A map of model attribute sets.
     * @param responseQueue       A shared queue for sending responses.
     * @param globalAgentStore    A shared store for managing agents.
     */
    public CoordinatorRequestHandler(
            String threadName,
            int numOfCores,
            Map<String, ModelAttributeSet> modelAttributeSetMap,
            BlockingQueue<Response> responseQueue,
            AgentStore globalAgentStore
    ) {
        this.threadName = threadName;
        this.numOfCores = numOfCores;
        this.modelAttributeSetMap = modelAttributeSetMap;
        this.responseQueue = responseQueue;
        this.globalAgentStore = globalAgentStore;
    }

    // Getters for handler attributes
    protected String getThreadName() {
        return threadName;
    }

    protected int getNumOfCores() {
        return numOfCores;
    }

    protected Map<String, ModelAttributeSet> getModelAttributeSetMap() {
        return modelAttributeSetMap;
    }

    protected BlockingQueue<Response> getResponseQueue() {
        return responseQueue;
    }

    protected AgentStore getGlobalAgentStore() {
        return globalAgentStore;
    }

    protected List<String> getWorkersWaiting() {
        return workersWaiting;
    }

    protected void setWorkersWaiting(List<String> workersWaiting) {
        this.workersWaiting = workersWaiting;
    }

    /**
     * Handles a specific request type. Must be implemented by subclasses.
     *
     * @param request The request to process.
     * @throws InterruptedException If interrupted while processing the request.
     */
    public abstract void handleRequest(Request request) throws InterruptedException;

    // Subclasses implementing specific request handling behaviours.

    public static class AllWorkersFinishTick extends CoordinatorRequestHandler {
        public AllWorkersFinishTick(
                String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap,
                BlockingQueue<Response> responseQueue, AgentStore globalAgentStore
        ) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());
            if (getWorkersWaiting().size() == getNumOfCores()) {
                for (String worker : getWorkersWaiting()) {
                    getResponseQueue().put(new Response(getThreadName(), worker, ResponseCodes.ALL_WORKERS_FINISH_TICK, null));
                }
                setWorkersWaiting(new ArrayList<>());
            }
        }
    }

    public static class AllWorkersUpdateCoordinator extends CoordinatorRequestHandler {
        public AllWorkersUpdateCoordinator(
                String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap,
                BlockingQueue<Response> responseQueue, AgentStore globalAgentStore
        ) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());
            if (getWorkersWaiting().size() == getNumOfCores()) {
                for (String worker : getWorkersWaiting()) {
                    getResponseQueue().put(new Response(getThreadName(), worker, ResponseCodes.ALL_WORKERS_UPDATE_COORDINATOR, null));
                }
                setWorkersWaiting(new ArrayList<>());

                for (ModelAttributeSet modelAttributeSet : getModelAttributeSetMap().values()) {
                    modelAttributeSet.run();
                }
            }
        }
    }

    public static class AgentAccess extends CoordinatorRequestHandler {
        public AgentAccess(
                String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap,
                BlockingQueue<Response> responseQueue, AgentStore globalAgentStore
        ) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseCodes.AGENT_ACCESS,
                    getGlobalAgentStore().get((String) request.getPayload())));
        }
    }

    public static class UpdateCoordinatorAgents extends CoordinatorRequestHandler {
        public UpdateCoordinatorAgents(
                String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap,
                BlockingQueue<Response> responseQueue, AgentStore globalAgentStore
        ) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) {
            getGlobalAgentStore().update((AgentStore) request.getPayload());
        }
    }

    public static class FilteredAgentsAccess extends CoordinatorRequestHandler {
        public FilteredAgentsAccess(
                String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap,
                BlockingQueue<Response> responseQueue, AgentStore globalAgentStore
        ) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(),
                    ResponseCodes.FILTERED_AGENTS_ACCESS,
                    getGlobalAgentStore().getFilteredAgents((Predicate<Agent>) request.getPayload())));
        }
    }

    public static class ModelAttributesAccess extends CoordinatorRequestHandler {
        public ModelAttributesAccess(
                String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap,
                BlockingQueue<Response> responseQueue, AgentStore globalAgentStore
        ) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(),
                    ResponseCodes.MODEL_ATTRIBUTES_ACCESS, getModelAttributeSetMap()));
        }
    }
}
