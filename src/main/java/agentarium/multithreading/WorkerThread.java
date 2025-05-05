package agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.agents.AgentSet;
import agentarium.multithreading.requestresponse.RequestResponseController;
import agentarium.multithreading.requestresponse.RequestResponseInterface;
import agentarium.multithreading.utils.WorkerCache;
import agentarium.results.AgentResults;
import agentarium.results.Results;
import agentarium.scheduler.ModelScheduler;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class WorkerThread<T extends Results> implements Callable<Results> {
    private final String threadName;
    private final ModelSettings settings;
    private final ModelScheduler scheduler;
    private final RequestResponseController requestResponseController;
    private final AgentSet agents;
    private final AgentSet updatedAgents;


    public WorkerThread(String threadName,
                        ModelSettings settings,
                        ModelScheduler scheduler,
                        RequestResponseController requestResponseController,
                        AgentSet agents) {
        this.threadName = threadName;
        this.settings = settings;
        this.scheduler = scheduler;
        this.requestResponseController = requestResponseController;
        this.agents = agents;
        this.updatedAgents = this.agents.duplicate();
    }

    @Override
    public Results call() throws InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        WorkerCache cache;
        if (settings.getIsCacheUsed())
            cache = new WorkerCache(settings.getDoAgentStoresHoldAgentCopies());
        else
            cache = null;

        RequestResponseInterface requestResponseInterface = requestResponseController.getInterface(threadName);

        if (settings.getAreProcessesSynced())
            requestResponseInterface.updateCoordinatorAgents(agents);

        for (int tick = 0; tick < settings.getTotalNumOfTicks(); tick++) {
            scheduler.runTick(agents);

            if (settings.getAreProcessesSynced()) {
                requestResponseInterface.waitUntilAllWorkersFinishTick();
                agents.add(updatedAgents);
                requestResponseInterface.updateCoordinatorAgents(agents);
                requestResponseInterface.waitUntilAllWorkersUpdateCoordinator();
            }

            if (settings.getIsCacheUsed())
                cache.clear();
        }

        agents.setup();
        AgentResults agentResults = new AgentResults(agents);
        Results results = settings.getResultsClass().getDeclaredConstructor().newInstance();
        results.setAgentNames(agents);
        results.setAgentResults(agentResults);
        return results;
    }
}
