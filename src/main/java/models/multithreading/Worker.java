package models.multithreading;

import models.ModelResults;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.requestresponse.Request;
import models.multithreading.requestresponse.Response;
import models.multithreading.threadutilities.AgentStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class Worker implements Callable<ModelResults> {
    private final String threadName;
    private final AgentStore agentStore;
    private final List<ModelAttributeSet> modelAttributeSetList;
    private final Map<String, ModelAttributeSet> modelAttributeSetMap;
    private final boolean areProcessesSynced;
    private final boolean doAgentStoresHoldAgentCopies;
    private final boolean isCacheUsed;
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    public Worker(String threadName,
                  AgentStore agentStore,
                  List<ModelAttributeSet> modelAttributeSetList,
                  Map<String, ModelAttributeSet> modelAttributeSetMap,
                  boolean areProcessesSynced,
                  boolean doAgentStoresHoldAgentCopies,
                  boolean isCacheUsed,
                  BlockingQueue<Request> requestQueue,
                  BlockingQueue<Response> responseQueue) {
        this.threadName = threadName;
        this.agentStore = agentStore;
        this.modelAttributeSetList = modelAttributeSetList;
        this.modelAttributeSetMap = modelAttributeSetMap;
        this.areProcessesSynced = areProcessesSynced;
        this.doAgentStoresHoldAgentCopies = doAgentStoresHoldAgentCopies;
        this.isCacheUsed = isCacheUsed;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    @Override
    public ModelResults call() throws Exception {
        return null;
    }
}
