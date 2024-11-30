package models.agentgenerator;

import agents.Agent;
import models.Model;

import java.util.List;

public abstract class AgentGenerator {
    private Model model;

    public void setAssociatedModel(Model model) {
        this.model = model;
    }

    public Model getAssociatedModel() {
        return model;
    }

    public abstract List<List<Agent>> getAgentsForEachCore(List<Agent> agentsList);
    public abstract Agent generateAgent();
}
