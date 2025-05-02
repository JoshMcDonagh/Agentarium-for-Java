package agentarium;

import agentarium.agents.AgentGenerator;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.environments.EnvironmentGenerator;
import agentarium.scheduler.ModelScheduler;

import java.util.List;

public class Model {
    private ModelSettings settings;
    private AgentGenerator agentGenerator;
    private EnvironmentGenerator environmentGenerator;
    private ModelScheduler scheduler;

    public Model(ModelSettings settings, AgentGenerator agentGenerator, EnvironmentGenerator environmentGenerator, ModelScheduler scheduler) {
        this.settings = settings;
        this.agentGenerator = agentGenerator;
        this.environmentGenerator = environmentGenerator;
        this.scheduler = scheduler;
    }

    public void run() {
        List<AgentSet> agentsForEachCore = agentGenerator.getAgentsForEachCore(settings);
        Environment environment = environmentGenerator.generateEnvironment(settings);

        environment.setup();
    }
}
