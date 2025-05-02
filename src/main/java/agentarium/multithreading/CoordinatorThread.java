package agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.multithreading.requestresponse.RequestResponseController;

public class CoordinatorThread implements Runnable {
    private final String threadName;
    private final ModelSettings settings;
    private final Environment environment;

    private final RequestResponseController requestResponseController;

    public CoordinatorThread(String name,
                             ModelSettings settings,
                             Environment environment,
                             RequestResponseController requestResponseController) {
        this.threadName = name;
        this.settings = settings;
        this.environment = environment;
        this.requestResponseController = requestResponseController;
    }

    @Override
    public void run() {
        AgentSet globalAgentSet = new AgentSet();

    }
}
