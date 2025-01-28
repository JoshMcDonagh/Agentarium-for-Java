package agentarium;

import agentarium.agents.AgentGenerator;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.environments.EnvironmentGenerator;
import agentarium.scheduler.ModelScheduler;

import java.util.List;

public class Model {
    private ModelSettings modelSettings;
    private AgentGenerator agentGenerator;
    private EnvironmentGenerator environmentGenerator;
    private ModelScheduler modelScheduler;

    public Model(ModelSettings modelSettings, AgentGenerator agentGenerator, EnvironmentGenerator environmentGenerator, ModelScheduler modelScheduler) {
        this.modelSettings = modelSettings;
        this.agentGenerator = agentGenerator;
        this.environmentGenerator = environmentGenerator;
        this.modelScheduler = modelScheduler;
    }

    public void run() {
        List<AgentSet> agentsForEachCore = agentGenerator.getAgentsForEachCore(modelSettings);
        Environment environment = environmentGenerator.generateEnvironment(modelSettings);
    }
}
