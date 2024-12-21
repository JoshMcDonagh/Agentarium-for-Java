package models.agentgenerator;

import agents.Agent;
import models.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for generating and managing agents within a simulation model.
 * This class provides a framework for associating agents with a model, generating agents,
 * and distributing them across computational cores.
 */
public abstract class AgentGenerator {

    // The model associated with this agent generator.
    private Model model;

    /**
     * Sets the model associated with this agent generator.
     *
     * @param model The model to associate with this generator.
     */
    public void setAssociatedModel(Model model) {
        this.model = model;
    }

    /**
     * Retrieves the model associated with this agent generator.
     *
     * @return The associated model.
     */
    public Model getAssociatedModel() {
        return model;
    }

    /**
     * Generates a specified number of agents using the `generateAgent` method.
     *
     * @param numOfAgents The number of agents to generate.
     * @return A list of generated agents.
     */
    public List<Agent> generateAgents(int numOfAgents) {
        // Initialise a list to store generated agents.
        List<Agent> agents = new ArrayList<Agent>();

        // Generate the specified number of agents and add them to the list.
        for (int i = 0; i < numOfAgents; i++)
            agents.add(generateAgent());

        return agents;
    }

    /**
     * Abstract method to determine how agents should be distributed across computational cores.
     *
     * @param agentsList The list of agents to distribute.
     * @return A list of lists, where each inner list represents the agents for a single core.
     */
    public abstract List<List<Agent>> getAgentsForEachCore(List<Agent> agentsList);

    /**
     * Abstract method to define the process for generating a single agent.
     *
     * @return The generated agent.
     */
    protected abstract Agent generateAgent();
}
