package models;

import agents.Agent;
import models.agentgenerator.AgentGenerator;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.Coordinator;
import models.multithreading.Worker;
import models.multithreading.requestresponse.Request;
import models.multithreading.requestresponse.Response;
import models.multithreading.threadutilities.AgentStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Model {
    private final int numOfAgents;
    private final ModelClock clock;
    private final AgentGenerator agentGenerator;
    private final ModelResults results;

    private int numOfCores = 1;
    private final Map<String, ModelAttributeSet> modelAttributeSetMap = new HashMap<String, ModelAttributeSet>();
    private final List<ModelAttributeSet> modelAttributeSetList = new ArrayList<ModelAttributeSet>();
    private boolean areProcessesSynced = false;
    private boolean doAgentStoresHoldAgentCopies = true;
    private boolean isAgentCacheUsed = false;

    public Model(int numOfAgents, ModelClock clock, AgentGenerator agentGenerator, ModelResults results) {
        this.numOfAgents = numOfAgents;
        this.clock = clock;
        this.agentGenerator = agentGenerator;
        this.agentGenerator.setAssociatedModel(this);
        this.results = results;
    }

    public void addAttributeSet(ModelAttributeSet modelAttributeSet) {
        modelAttributeSetMap.put(modelAttributeSet.name(), modelAttributeSet);
        modelAttributeSetList.add(modelAttributeSet);
    }

    public void addAttributeSets(ModelAttributeSet[] modelAttributeSets) {
        for (ModelAttributeSet modelAttributeSet : modelAttributeSets) {
            addAttributeSet(modelAttributeSet);
        }
    }

    public void setNumberOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public void setAreProcessesSynced(boolean areProcessesSynced) {
        this.areProcessesSynced = areProcessesSynced;
    }

    public void setDoAgentStoresHoldAgentCopies() {
        doAgentStoresHoldAgentCopies = true;
    }

    public void setIsAgentCacheUsed(boolean isAgentCacheUsed) {
        this.isAgentCacheUsed = isAgentCacheUsed;
    }

    public int numberOfAgents() {
        return numOfAgents;
    }

    public ModelClock clock() {
        return clock;
    }

    public void run() {
        List<Agent> agents = agentGenerator.generateAgents(numOfAgents);
        List<List<Agent>> agentsForEachCore = agentGenerator.getAgentsForEachCore(agents);

        ExecutorService executorService = Executors.newFixedThreadPool(numOfCores);
        List<Future<ModelResults>> futures = new ArrayList<Future<ModelResults>>();

        BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<Request>();
        BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<Response>();
        Thread coordinatorThread = null;

        if (areProcessesSynced) {
            Coordinator coordinator = new Coordinator(String.valueOf(numOfCores), numOfCores, modelAttributeSetList, modelAttributeSetMap, requestQueue, responseQueue);
            coordinatorThread = new Thread(coordinator);
            coordinatorThread.start();
        }

        for (int coreIndex = 0; coreIndex < numOfCores; coreIndex++) {
            AgentStore coreAgentStore = new AgentStore(doAgentStoresHoldAgentCopies);
            coreAgentStore.addAgents(agentsForEachCore.get(coreIndex));
            ModelResults coreResults = results.duplicate();

            Callable<ModelResults> worker = new Worker<>(
                    String.valueOf(coreIndex),
                    clock,
                    coreAgentStore,
                    modelAttributeSetList,
                    modelAttributeSetMap,
                    results.getClass(),
                    areProcessesSynced,
                    doAgentStoresHoldAgentCopies,
                    isAgentCacheUsed,
                    requestQueue,
                    responseQueue
            );

            futures.add(executorService.submit(worker));
        }

        // Wait for all worker tasks to complete and merge results
        try {
            for (Future<ModelResults> future : futures) {
                ModelResults coreResult = future.get();
                results.mergeWith(coreResult);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        // Stop the coordinator thread if used
        if (areProcessesSynced && coordinatorThread != null) {
            coordinatorThread.interrupt();
            try {
                coordinatorThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
