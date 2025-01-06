package models.multithreading.threadutilities;

import agents.Agent;
import models.modelattributes.ModelAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The `WorkerCache` class provides a centralised cache for agents, agent filters,
 * and model attribute sets to facilitate multi-threaded operations in simulations.
 * <p>
 * This class integrates with the `AgentStore` to manage agents and maintains a
 * collection of filters and model attribute sets for efficient querying and processing.
 */
public class WorkerCache {

    // List to store agent filters used for querying agents.
    private final List<Predicate<Agent>> agentFiltersCacheList = new ArrayList<>();

    // Map to store model attribute sets by their names.
    private final Map<String, ModelAttributeSet> modelAttributeSetMap = new HashMap<>();

    // Cache of agents managed using an `AgentStore`.
    private final AgentStore agentStoreCache;

    /**
     * Constructs a `WorkerCache` with the specified agent storage configuration.
     *
     * @param isAgentCopiesHeld If true, agents are stored as copies without records; otherwise, original agents are stored.
     */
    public WorkerCache(boolean isAgentCopiesHeld) {
        agentStoreCache = new AgentStore(isAgentCopiesHeld);
    }

    /**
     * Clears all caches, including agent filters, model attribute sets, and stored agents.
     */
    public void clear() {
        agentFiltersCacheList.clear();
        modelAttributeSetMap.clear();
        agentStoreCache.clear();
    }

    /**
     * Checks if a given agent filter exists in the cache.
     *
     * @param agentFilter The agent filter to check.
     * @return True if the filter exists, otherwise false.
     */
    public boolean doesAgentFilterExist(Predicate<Agent> agentFilter) {
        return agentFiltersCacheList.contains(agentFilter);
    }

    /**
     * Adds a new agent filter to the cache.
     *
     * @param agentFilter The agent filter to add.
     */
    public void addAgentFilter(Predicate<Agent> agentFilter) {
        agentFiltersCacheList.add(agentFilter);
    }

    /**
     * Retrieves a list of agents matching a given filter.
     *
     * @param agentFilter The filter to apply to agents.
     * @return A list of agents that satisfy the filter condition.
     */
    public List<Agent> getFilteredAgents(Predicate<Agent> agentFilter) {
        return agentStoreCache.getFilteredAgents(agentFilter);
    }

    /**
     * Checks if an agent exists in the cache by name.
     *
     * @param agentName The name of the agent to check.
     * @return True if the agent exists, otherwise false.
     */
    public boolean doesAgentExist(String agentName) {
        return agentStoreCache.doesAgentExist(agentName);
    }

    /**
     * Retrieves an agent from the cache by name.
     *
     * @param agentName The name of the agent to retrieve.
     * @return The agent with the specified name, or null if not found.
     */
    public Agent getAgent(String agentName) {
        return agentStoreCache.get(agentName);
    }

    /**
     * Adds a single agent to the cache.
     *
     * @param agent The agent to add.
     */
    public void addAgent(Agent agent) {
        agentStoreCache.addAgent(agent);
    }

    /**
     * Adds multiple agents to the cache.
     *
     * @param agents The list of agents to add.
     */
    public void addAgents(List<Agent> agents) {
        agentStoreCache.addAgents(agents);
    }

    /**
     * Checks if there are any model attribute sets in the cache.
     *
     * @return True if there is at least one model attribute set, otherwise false.
     */
    public boolean doModelAttributeSetsExist() {
        return !modelAttributeSetMap.isEmpty();
    }

    /**
     * Adds or updates a model attribute set in the cache.
     *
     * @param modelAttributeSet The model attribute set to add or update.
     */
    public void setModelAttributeSet(ModelAttributeSet modelAttributeSet) {
        modelAttributeSetMap.put(modelAttributeSet.name(), modelAttributeSet);
    }

    /**
     * Retrieves a model attribute set from the cache by name.
     *
     * @param attributeSetName The name of the attribute set to retrieve.
     * @return The model attribute set with the specified name, or null if not found.
     */
    public ModelAttributeSet getModelAttributeSet(String attributeSetName) {
        return modelAttributeSetMap.get(attributeSetName);
    }
}
