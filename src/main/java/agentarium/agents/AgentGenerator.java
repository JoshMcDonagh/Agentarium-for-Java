package agentarium.agents;

import agentarium.ModelSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for generating agent populations in a simulation.
 *
 * <p>This class provides methods to:
 * <ul>
 *     <li>Generate a full set of agents based on model settings</li>
 *     <li>Distribute agents evenly across multiple processing cores</li>
 * </ul>
 *
 * <p>Concrete subclasses must implement the {@link #generateAgent(ModelSettings)} method,
 * which defines how individual agents are constructed.
 */
public abstract class AgentGenerator {

    /**
     * Generates a complete {@link AgentSet} based on the number of agents specified in the model settings.
     *
     * @param modelSettings the simulation configuration containing the agent count
     * @return an {@link AgentSet} containing all generated agents
     */
    public AgentSet generateAgents(ModelSettings modelSettings) {
        AgentSet agents = new AgentSet();
        int numOfAgents = modelSettings.getNumOfAgents();

        for (int i = 0; i < numOfAgents; i++) {
            agents.add(generateAgent(modelSettings));
        }

        return agents;
    }

    /**
     * Distributes agents across processing cores in a round-robin fashion.
     * This ensures an even workload split for multithreaded simulations.
     *
     * @param modelSettings the simulation settings containing agent and core counts
     * @return a list of {@link AgentSet} objects, one per core
     */
    public List<AgentSet> getAgentsForEachCore(ModelSettings modelSettings) {
        AgentSet agents = generateAgents(modelSettings);
        int numOfCores = modelSettings.getNumOfCores();

        // If no cores are defined, return an empty list
        if (numOfCores < 1)
            return new ArrayList<>();

        // If only one core is used, assign all agents to it
        if (numOfCores == 1) {
            List<AgentSet> singleCoreList = new ArrayList<>();
            singleCoreList.add(agents);
            return singleCoreList;
        }

        // Prepare empty agent sets for each core
        List<AgentSet> agentsForEachCore = new ArrayList<>();
        for (int i = 0; i < numOfCores; i++)
            agentsForEachCore.add(new AgentSet());

        // Distribute agents evenly across cores (round-robin)
        int core = 0;
        for (Agent agent : agents) {
            agentsForEachCore.get(core).add(agent);
            core = (core + 1) % numOfCores;
        }

        return agentsForEachCore;
    }

    /**
     * Abstract method for generating a single agent instance.
     * Must be implemented by concrete subclasses.
     *
     * @param modelSettings the model settings passed to the agent during creation
     * @return a new {@link Agent} instance
     */
    protected abstract Agent generateAgent(ModelSettings modelSettings);
}
