package agentarium;

import agentarium.agents.Agent;
import agentarium.agents.AgentGenerator;
import agentarium.environments.Environment;
import agentarium.environments.EnvironmentGenerator;

import java.util.List;

public class Model {
    private ModelSettings modelSettings;
    private AgentGenerator agentGenerator;
    private EnvironmentGenerator environmentGenerator;

    public Model(ModelSettings modelSettings, AgentGenerator agentGenerator, EnvironmentGenerator environmentGenerator) {
        this.agentGenerator = agentGenerator;
        this.environmentGenerator = environmentGenerator;
    }

    public void run() {
        List<List<Agent>> agentsForEachCore = agentGenerator.getAgentsForEachCode(modelSettings.getNumOfAgents(), modelSettings.getNumOfCores(), modelSettings);
        Environment environment = environmentGenerator.generateEnvironment(modelSettings);
    }
}
