package models.multithreading;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.requestresponse.CoordinatorRequestHandler;
import models.multithreading.requestresponse.Request;
import models.multithreading.requestresponse.Response;
import models.multithreading.threadutilities.AgentStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Coordinator implements Runnable {
    private final String threadName;
    private final int numOfCores;
    private final List<ModelAttributeSet> modelAttributeSetList;
    private final Map<String, ModelAttributeSet> modelAttributeSetMap;
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    public Coordinator(String threadName, int numOfCores, List<ModelAttributeSet> modelAttributeSetList, Map<String, ModelAttributeSet> modelAttributeSetMap, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.threadName = threadName;
        this.numOfCores = numOfCores;
        this.modelAttributeSetList = modelAttributeSetList;
        this.modelAttributeSetMap = modelAttributeSetMap;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {
        AgentStore globalAgentStore = new AgentStore();
        for (ModelAttributeSet modelAttributeSet : modelAttributeSetList)
            modelAttributeSet.setAgentStore(globalAgentStore);
        CoordinatorRequestHandler.initialise(threadName, numOfCores, modelAttributeSetMap, responseQueue, globalAgentStore);

        while (true) {
            if (!requestQueue.isEmpty()) {
                Request request = requestQueue.poll();
                try {
                    CoordinatorRequestHandler.handleCoordinatorRequest(request);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
