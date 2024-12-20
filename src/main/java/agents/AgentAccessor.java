package agents;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.requestresponse.RequestResponseOperator;
import models.multithreading.threadutilities.AgentStore;
import models.multithreading.threadutilities.WorkerCache;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The `AgentAccessor` class provides methods for accessing and managing agents and their attributes
 * across local and distributed processes. It handles agent retrieval, filtering, caching, and
 * communication with a central coordinator when required.
 */
public class AgentAccessor {
    private Agent agent;
    private List<ModelAttributeSet> modelAttributeSetList;
    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private RequestResponseOperator requestResponseOperator;
    private AgentStore localAgentStore;
    private boolean areProcessesSynced;
    private boolean isCacheUsed;
    private WorkerCache cache;

    /**
     * Constructs an `AgentAccessor` with the provided parameters for managing agents and attributes.
     *
     * @param agent                 The agent associated with this accessor.
     * @param modelAttributeSetList The list of model attribute sets.
     * @param modelAttributeSetMap  A map of attribute set names to model attribute sets.
     * @param requestResponseOperator The operator for handling inter-process requests and responses.
     * @param localAgentStore       The local store for agents in the current process.
     * @param areProcessesSynced    Indicates whether processes are synchronised for communication.
     * @param isCacheUsed           Indicates whether caching is enabled for agent and attribute access.
     * @param cache                 The worker cache for storing agent and attribute data.
     */
    public AgentAccessor(
            Agent agent,
            List<ModelAttributeSet> modelAttributeSetList,
            Map<String, ModelAttributeSet> modelAttributeSetMap,
            RequestResponseOperator requestResponseOperator,
            AgentStore localAgentStore,
            boolean areProcessesSynced,
            boolean isCacheUsed,
            WorkerCache cache) {
        this.agent = agent;
        this.modelAttributeSetList = modelAttributeSetList;
        this.modelAttributeSetMap = modelAttributeSetMap;
        this.requestResponseOperator = requestResponseOperator;
        this.localAgentStore = localAgentStore;
        this.areProcessesSynced = areProcessesSynced;
        this.isCacheUsed = isCacheUsed;
        this.cache = cache;
    }

    /**
     * Checks whether an agent exists in the current core (local store).
     *
     * @param agentName The name of the agent to check.
     * @return `true` if the agent exists in the local store; otherwise, `false`.
     */
    public boolean doesAgentExistInThisCore(String agentName) {
        return localAgentStore.doesAgentExist(agentName);
    }

    /**
     * Retrieves an agent by its name, checking the local store, cache, or coordinator as necessary.
     *
     * @param targetAgentName The name of the agent to retrieve.
     * @return The `Agent` object if found, or `null` if the agent does not exist.
     */
    public Agent getAgentByName(String targetAgentName) {
        // Check the local store
        if (doesAgentExistInThisCore(targetAgentName)) {
            return localAgentStore.get(targetAgentName);
        }

        // Check the cache if enabled
        if (isCacheUsed && cache.doesAgentExist(targetAgentName)) {
            return cache.getAgent(targetAgentName);
        }

        // Request from the coordinator if processes are synchronised
        if (!areProcessesSynced) {
            return null;
        }

        try {
            Agent requestedAgent = requestResponseOperator.getAgentFromCoordinator(agent.name(), targetAgentName);
            if (isCacheUsed) {
                cache.addAgent(requestedAgent);
            }
            return requestedAgent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a filtered list of agents based on a predicate, using the local store, cache, or coordinator.
     *
     * @param agentFilter A predicate defining the filter criteria.
     * @return A list of agents matching the filter, or `null` if an error occurs.
     */
    public List<Agent> getFilteredAgents(Predicate<Agent> agentFilter) {
        // Check the cache if enabled
        if (isCacheUsed && cache.doesAgentFilterExist(agentFilter)) {
            return cache.getFilteredAgents(agentFilter);
        }

        List<Agent> filteredAgents;
        // Request from the coordinator if processes are synchronised
        if (areProcessesSynced) {
            try {
                filteredAgents = requestResponseOperator.getFilteredAgentsFromCoordinator(agent.name(), agentFilter);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            filteredAgents = localAgentStore.getFilteredAgents(agentFilter);
        }

        // Cache the result if enabled
        if (isCacheUsed) {
            cache.addAgentFilter(agentFilter);
            cache.addAgents(filteredAgents);
        }

        return filteredAgents;
    }

    /**
     * Retrieves a model attribute set by its name, using the local store, cache, or coordinator as necessary.
     *
     * @param attributeName The name of the model attribute set to retrieve.
     * @return The `ModelAttributeSet` object if found, or `null` if not found or an error occurs.
     */
    public ModelAttributeSet getModelAttributeSet(String attributeName) {
        // Check the cache if enabled
        if (isCacheUsed && cache.doModelAttributeSetsExist()) {
            return cache.getModelAttributeSet(attributeName);
        }

        ModelAttributeSet requestedModelAttributeSet;
        // Request from the coordinator if processes are synchronised
        if (areProcessesSynced) {
            try {
                Map<String, ModelAttributeSet> modelAttributeSets = requestResponseOperator.getModelAttributesFromCoordinator(agent.name());
                requestedModelAttributeSet = modelAttributeSets.get(attributeName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            requestedModelAttributeSet = modelAttributeSetMap.get(attributeName);
        }

        // Cache the result if enabled
        if (isCacheUsed) {
            cache.setModelAttributeSet(requestedModelAttributeSet);
        }

        return requestedModelAttributeSet;
    }
}
