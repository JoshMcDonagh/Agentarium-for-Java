package agentarium.multithreading;

import agentarium.ModelSettings;
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

    public Worker(String name, ModelSettings settings, Environment environment, ModelScheduler scheduler, RequestResponseController requestResponseController) {
        this.name = name;
        this.settings = settings;
        this.environment = environment;
        this.scheduler = scheduler;
        this.requestResponseController = requestResponseController;
    }

    @Override
    public Results call() throws Exception {
        WorkerCache cache;
        if (settings.getIsCacheUsed())
            cache = new WorkerCache(settings.getDoAgentStoresHoldAgentCopies());
        else
            cache = null;
    }
}
