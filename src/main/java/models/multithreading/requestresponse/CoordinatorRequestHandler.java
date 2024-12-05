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

public abstract class CoordinatorRequestHandler {
    private static Map<String, CoordinatorRequestHandler> requestHandlersMap;

    public static void initialise(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
        requestHandlersMap = new HashMap<String, CoordinatorRequestHandler>();
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

    public static void handleCoordinatorRequest(Request request) throws InterruptedException {
        requestHandlersMap.get(request.getRequestCode()).handleRequest(request);
    }

    private final String threadName;
    private final int numOfCores;
    private final Map<String, ModelAttributeSet> modelAttributeSetMap;
    private final BlockingQueue<Response> responseQueue;
    private final AgentStore globalAgentStore;
    private List<String> workersWaiting = new ArrayList<String>();

    public CoordinatorRequestHandler(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
        this.threadName = threadName;
        this.numOfCores = numOfCores;
        this.modelAttributeSetMap = modelAttributeSetMap;
        this.responseQueue = responseQueue;
        this.globalAgentStore = globalAgentStore;
    }

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

    protected void setWorkersWaiting(List<String> workersWaiting) {
        this.workersWaiting = workersWaiting;
    }

    protected List<String> getWorkersWaiting() {
        return workersWaiting;
    }

    public abstract void handleRequest(Request request) throws InterruptedException;

    public static class AllWorkersFinishTick extends CoordinatorRequestHandler {
        public AllWorkersFinishTick(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());

            if (getWorkersWaiting().size() == getNumOfCores()) {
                for (String worker : getWorkersWaiting())
                    getResponseQueue().put(new Response(getThreadName(), worker, ResponseCodes.ALL_WORKERS_FINISH_TICK, null));
                setWorkersWaiting(new ArrayList<String>());
            }
        }
    }

    public static class AllWorkersUpdateCoordinator extends CoordinatorRequestHandler {
        public AllWorkersUpdateCoordinator(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getWorkersWaiting().add(request.getRequester());

            if (getWorkersWaiting().size() == getNumOfCores()) {
                for (String worker : getWorkersWaiting())
                    getResponseQueue().put(new Response(getThreadName(), worker, ResponseCodes.ALL_WORKERS_UPDATE_COORDINATOR, null));
                setWorkersWaiting(new ArrayList<String>());

                for (ModelAttributeSet modelAttributeSet : getModelAttributeSetMap().values())
                    modelAttributeSet.run();
            }
        }
    }

    public static class AgentAccess extends CoordinatorRequestHandler {
        public AgentAccess(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseCodes.AGENT_ACCESS, getGlobalAgentStore().get((String) request.getPayload())));
        }
    }

    public static class UpdateCoordinatorAgents extends CoordinatorRequestHandler {
        public UpdateCoordinatorAgents(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getGlobalAgentStore().update((AgentStore) request.getPayload());
        }
    }

    public static class FilteredAgentsAccess extends CoordinatorRequestHandler {
        public FilteredAgentsAccess(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseCodes.FILTERED_AGENTS_ACCESS, getGlobalAgentStore().getFilteredAgents((Predicate<Agent>) request.getPayload())));
        }
    }

    public static class ModelAttributesAccess extends CoordinatorRequestHandler {
        public ModelAttributesAccess(String threadName, int numOfCores, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Response> responseQueue, AgentStore globalAgentStore) {
            super(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);
        }

        @Override
        public void handleRequest(Request request) throws InterruptedException {
            getResponseQueue().put(new Response(getThreadName(), request.getRequester(), ResponseCodes.MODEL_ATTRIBUTES_ACCESS, getModelAttributeSetMap()));
        }
    }
}
