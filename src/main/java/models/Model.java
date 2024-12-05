package models;

import agents.Agent;
import models.agentgenerator.AgentGenerator;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.Coordinator;
import models.multithreading.requestresponse.Request;
import models.multithreading.requestresponse.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Model {
    private int numOfAgents;
    private ModelClock clock;
    private AgentGenerator agentGenerator;
    private ModelResults results;

    private int numOfCores = 1;
    private List<ModelResults> resultsPerProcess = new ArrayList<ModelResults>();
    private Map<String, ModelAttributeSet> modelAttributeSetMap = new HashMap<String, ModelAttributeSet>();
    private List<ModelAttributeSet> modelAttributeSetList = new ArrayList<ModelAttributeSet>();
    private boolean areProcessesSynced = false;
    private boolean doProcessStoresHoldAgentCopies = true;
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

    public void setDoProcessStoresHoldAgentCopies() {
        doProcessStoresHoldAgentCopies = true;
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
            Coordinator coordinatorTask = new Coordinator(numOfCores, modelAttributeSetList, requestQueue, responseQueue);
            coordinatorThread = new Thread(coordinatorTask);
            coordinatorThread.start();
        }

        for (int coreIndex = 0; coreIndex < numOfCores; coreIndex++) {
            List<Agent> coreAgents = agentsForEachCore.get(coreIndex);
            ModelResults coreResults = results.duplicate();

            Callable<ModelResults> workerTask = new WorkerTask(
                    coreIndex,
                    coreAgents,
                    clock,
                    areProcessesSynced,
                    modelAttributeSetList,
                    doProcessStoresHoldAgentCopies,
                    isAgentCacheUsed,
                    coreResults
            );

            futures.add(executorService.submit(workerTask));
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
