package models.agentgenerator;

import agents.Agent;
import models.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class AgentGenerator {
    private Model model;

    public void setAssociatedModel(Model model) {
        this.model = model;
    }

    public Model getAssociatedModel() {
        return model;
    }

    public List<Agent> generateAgents(int numOfAgents) {
        List<Agent> agents = new ArrayList<Agent>();
        for (int i = 0; i < numOfAgents; i++)
            agents.add(generateAgent());
        return agents;
    }

    public abstract List<List<Agent>> getAgentsForEachCore(List<Agent> agentsList);
    protected abstract Agent generateAgent();
}
