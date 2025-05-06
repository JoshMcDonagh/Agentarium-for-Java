package agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.multithreading.requestresponse.CoordinatorRequestHandler;
import agentarium.multithreading.requestresponse.Request;
import agentarium.multithreading.requestresponse.RequestResponseController;

/**
 * Coordinator thread responsible for managing synchronised access to shared simulation state
 * between multiple worker threads in a parallel simulation.
 *
 * <p>This class listens to the request queue and uses {@link CoordinatorRequestHandler}
 * to respond to agent/environment-related queries or updates.
 */
public class CoordinatorThread implements Runnable {

    /** A label or ID for this thread (used for logging or debugging) */
    private final String threadName;

    /** Global simulation configuration */
    private final ModelSettings settings;

    /** The environment shared across all workers */
    private final Environment environment;

    /** Controller that manages the request and response queues for inter-thread communication */
    private final RequestResponseController requestResponseController;

    /**
     * Constructs the coordinator thread with required references.
     *
     * @param name the thread name or ID
     * @param settings global model settings
     * @param environment the shared simulation environment
     * @param requestResponseController the controller managing request/response queues
     */
    public CoordinatorThread(String name,
                             ModelSettings settings,
                             Environment environment,
                             RequestResponseController requestResponseController) {
        this.threadName = name;
        this.settings = settings;
        this.environment = environment;
        this.requestResponseController = requestResponseController;
    }

    /**
     * Main execution loop for the coordinator thread.
     *
     * <p>Continuously listens for requests from worker threads and processes them.
     */
    @Override
    public void run() {
        AgentSet globalAgentSet = new AgentSet();

        // Initialise the request handler with access to the global agent state and environment
        CoordinatorRequestHandler.initialise(
                threadName,
                settings,
                requestResponseController.getResponseQueue(),
                globalAgentSet,
                environment);

        // Continuously poll for and handle incoming requests from workers
        while (true) {
            if (!requestResponseController.getRequestQueue().isEmpty()) {
                Request request = requestResponseController.getRequestQueue().poll();
                try {
                    CoordinatorRequestHandler.handleCoordinatorRequest(request);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
