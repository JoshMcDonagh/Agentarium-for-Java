package agentarium;

import agentarium.agents.AgentGenerator;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.environments.EnvironmentGenerator;
import agentarium.results.Results;
import agentarium.scheduler.ModelScheduler;

import java.util.List;

public class Model {
    private final ModelSettings settings;
    private final AgentGenerator agentGenerator;
    private final EnvironmentGenerator environmentGenerator;
    private ModelScheduler scheduler;

    public Model(ModelSettings settings,
                 AgentGenerator agentGenerator,
                 EnvironmentGenerator environmentGenerator,
                 ModelScheduler scheduler,
                 Results results) {
        this.settings = settings;
        this.agentGenerator = agentGenerator;
        this.environmentGenerator = environmentGenerator;
        this.scheduler = scheduler;
    }

    public Results run() {
        List<AgentSet> agentsForEachCore = agentGenerator.getAgentsForEachCore(settings);
        Environment environment = environmentGenerator.generateEnvironment(settings);

        environment.setup();
    }
}
