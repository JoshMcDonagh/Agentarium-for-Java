package models.multithreading;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.requestresponse.Request;
import models.multithreading.requestresponse.Response;
import models.multithreading.threadutilities.AgentStore;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Coordinator implements Runnable {
    private String threadName;
    private List<ModelAttributeSet> modelAttributeSetList;
    private BlockingQueue<Request> requestQueue;
    BlockingQueue<Response> responseQueue;

    public Coordinator(String threadName, List<ModelAttributeSet> modelAttributeSetList, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.threadName = threadName;
        this.modelAttributeSetList = modelAttributeSetList;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {
        AgentStore globalAgentStore = new AgentStore();
        for (ModelAttributeSet modelAttributeSet : modelAttributeSetList)
            modelAttributeSet.setAgentStore(globalAgentStore);
    }
}
