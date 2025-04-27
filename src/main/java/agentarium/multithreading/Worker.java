package agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.multithreading.utils.WorkerCache;
import agentarium.results.Results;
import agentarium.scheduler.ModelScheduler;

import java.util.concurrent.Callable;

public class Worker <T extends Results> implements Callable<Results> {
    private final String name;
    private final ModelSettings settings;
    private final Environment environment;
    private final ModelScheduler scheduler;
    private final RequestResponseController requestResponseController;
    private final AgentSet agents;
    private final AgentSet updatedAgents;

    public Worker(String name, ModelSettings settings, Environment environment, ModelScheduler scheduler, RequestResponseController requestResponseController, AgentSet agents) {
        this.name = name;
        this.settings = settings;
        this.environment = environment;
        this.scheduler = scheduler;
        this.requestResponseController = requestResponseController;
        this.agents = agents;
        this.updatedAgents = this.agents.duplicate();
    }

    @Override
    public Results call() throws Exception {
        WorkerCache cache;
        if (settings.getIsCacheUsed())
            cache = new WorkerCache(settings.getDoAgentStoresHoldAgentCopies());
        else
            cache = null;

        RequestResponseInterface requestResponseInterface = requestResponseController.getInterface(name);

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
    }
}
