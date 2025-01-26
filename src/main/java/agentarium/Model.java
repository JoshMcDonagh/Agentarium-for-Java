package agentarium;

import agentarium.agents.AgentGenerator;
import agentarium.environments.EnvironmentGenerator;

public class Model {
    private AgentGenerator agentGenerator;
    private EnvironmentGenerator environmentGenerator;

    public Model(AgentGenerator agentGenerator, EnvironmentGenerator environmentGenerator) {
        this.agentGenerator = agentGenerator;
        this.environmentGenerator = environmentGenerator;
    }
}
