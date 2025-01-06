package models.multithreading.threadutilities;

import agents.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The `AgentStore` class provides functionality to manage and store a collection of agents.
 * <p>
 * It supports storing agents as original instances or as copies (without records),
 * updating the store, and filtering agents based on criteria.
 */
public class AgentStore {

    // Flag to determine whether agent copies (without records) should be stored.
    private final boolean isStoringAgentCopies;

    // List of agents stored in the store.
    private List<Agent> agentsList = new ArrayList<>();

    // Map of agent names to agent instances for quick access.
    private Map<String, Agent> agentsMap = new HashMap<>();

    /**
     * Constructs an `AgentStore` with the specified configuration.
     *
     * @param isStoringAgentCopies If true, stores copies of agents (without records); otherwise, stores original agents.
     */
    public AgentStore(boolean isStoringAgentCopies) {
        this.isStoringAgentCopies = isStoringAgentCopies;
    }

    /**
     * Constructs an `AgentStore` that stores copies of agents by default.
     */
    public AgentStore() {
        this.isStoringAgentCopies = true;
    }

    /**
     * Clears the store, removing all stored agents.
     */
    public void clear() {
        agentsMap = new HashMap<>();
        agentsList = new ArrayList<>();
    }

    /**
     * Adds a single agent to the store.
     * <p>
     * If storing copies is enabled, a duplicate of the agent (without records) is added.
     *
     * @param agent The agent to add to the store.
     */
    public void addAgent(Agent agent) {
        Agent agentToStore = isStoringAgentCopies ? agent.duplicateWithoutRecords() : agent;

        agentsList.add(agentToStore);
        agentsMap.put(agentToStore.name(), agentToStore);
    }

    /**
     * Adds a list of agents to the store.
     * <p>
     * Each agent is processed according to the store's copy behaviour.
     *
     * @param agents The list of agents to add to the store.
     */
    public void addAgents(List<Agent> agents) {
        for (Agent agent : agents)
            addAgent(agent);
    }

    /**
     * Retrieves an agent from the store by name.
     *
     * @param agentName The name of the agent to retrieve.
     * @return The agent with the specified name, or null if no such agent exists.
     */
    public Agent get(String agentName) {
        return agentsMap.get(agentName);
    }

    /**
     * Retrieves a list of all agents stored in the store.
     *
     * @return A list of all agents.
     */
    public List<Agent> getAgentsList() {
        return agentsList;
    }

    /**
     * Updates the store with agents from another `AgentStore`.
     * <p>
     * Agents are added or replaced in the current store based on the names from the other store.
     * Copies of agents are stored if the copy behaviour is enabled.
     *
     * @param otherAgentStore The other `AgentStore` to update from.
     */
    public void update(AgentStore otherAgentStore) {
        for (Map.Entry<String, Agent> entry : otherAgentStore.agentsMap.entrySet()) {
            agentsMap.put(entry.getKey(),
                    isStoringAgentCopies ? entry.getValue().duplicateWithoutRecords() : entry.getValue());
        }
    }

    /**
     * Checks if an agent exists in the store by name.
     *
     * @param agentName The name of the agent to check for existence.
     * @return True if the agent exists; otherwise, false.
     */
    public boolean doesAgentExist(String agentName) {
        return agentsMap.containsKey(agentName);
    }

    /**
     * Retrieves a list of agents that satisfy a given filter condition.
     *
     * @param agentFilter A predicate defining the filter condition for agents.
     * @return A list of agents that meet the filter criteria.
     */
    public List<Agent> getFilteredAgents(Predicate<Agent> agentFilter) {
        List<Agent> filteredAgents = new ArrayList<>();

        for (Agent agent : agentsMap.values()) {
            if (agentFilter.test(agent))
                filteredAgents.add(agent);
        }

        return filteredAgents;
    }
}
