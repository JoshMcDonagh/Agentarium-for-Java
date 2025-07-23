package agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.agents.AgentSet;
import agentarium.multithreading.requestresponse.RequestResponseController;
import agentarium.multithreading.requestresponse.RequestResponseInterface;
import agentarium.multithreading.utils.WorkerCache;
import agentarium.results.AgentResults;
import agentarium.results.Results;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * Represents a single worker thread responsible for simulating one subset of agents
 * across the configured number of ticks.
 *
 * <p>Each {@code WorkerThread} operates on its own {@link AgentSet}, may use a local
 * {@link WorkerCache} for caching, and optionally communicates with a coordinator via
 * {@link RequestResponseInterface} if synchronisation is enabled.
 *
 * @param <T> the type of {@link Results} this worker will return
 */
public class WorkerThread<T extends Results> implements Callable<Results> {

    /** The name or ID assigned to this worker thread (usually based on core index) */
    private final String threadName;

    /** Global simulation settings shared across threads */
    private final ModelSettings settings;

    /** Interface to coordinate requests and responses across workers (if sync enabled) */
    private final RequestResponseController requestResponseController;

    /** The original set of agents this worker is responsible for simulating */
    private final AgentSet agents;

    /** A duplicate of the agent set to allow for safe merging during synchronisation */
    private final AgentSet updatedAgents;

    /**
     * Constructs a new worker thread to simulate a subset of agents.
     *
     * @param threadName the thread's name (typically its numeric ID as a string)
     * @param settings the simulation settings
     * @param requestResponseController the controller for cross-thread coordination
     * @param agents the agents assigned to this thread
     */
    public WorkerThread(String threadName,
                        ModelSettings settings,
                        RequestResponseController requestResponseController,
                        AgentSet agents) {
        this.threadName = threadName;
        this.settings = settings;
        this.requestResponseController = requestResponseController;
        this.agents = agents;
        this.updatedAgents = this.agents.duplicate();
    }

    /**
     * Executes the simulation loop for this worker.
     *
     * <p>This includes calling the scheduler each tick, synchronising with the coordinator
     * if needed, and collecting agent results after the simulation ends.
     *
     * @return a {@link Results} object containing final agent-level outputs
     */
    @Override
    public Results call() throws InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        WorkerCache cache = settings.getIsCacheUsed()
                ? new WorkerCache(settings.getDoAgentStoresHoldAgentCopies())
                : null;

        RequestResponseInterface requestResponseInterface = requestResponseController.getInterface(threadName);

        // Initial broadcast of agent state to coordinator
        if (settings.getAreProcessesSynced())
            requestResponseInterface.updateCoordinatorAgents(agents);

        agents.setup();

        // Simulation main loop
        for (int tick = 0; tick < settings.getTotalNumOfTicks(); tick++) {
            settings.getModelScheduler().runTick(agents);

            if (settings.getAreProcessesSynced()) {
                requestResponseInterface.waitUntilAllWorkersFinishTick();
                agents.add(updatedAgents); // Merge agent updates
                requestResponseInterface.updateCoordinatorAgents(agents);
                requestResponseInterface.waitUntilAllWorkersUpdateCoordinator();
            }

            if (cache != null)
                cache.clear();
        }

        // Final setup and result collection
        AgentResults agentResults = new AgentResults(agents);
        Results results = settings.getResults();
        results.setAgentNames(agents);
        results.setAgentResults(agentResults);

        return results;
    }
}
