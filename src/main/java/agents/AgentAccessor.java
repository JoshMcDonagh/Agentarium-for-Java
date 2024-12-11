package agents;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.requestresponse.CoordinatorRequestHandler;
import models.multithreading.requestresponse.RequestResponseOperator;
import models.multithreading.threadutilities.AgentStore;
import models.multithreading.threadutilities.WorkerCache;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AgentAccessor {
    private Agent agent;
    private List<ModelAttributeSet> modelAttributeSetList;
    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private RequestResponseOperator requestResponseOperator;
    private AgentStore localAgentStore;
    private boolean areProcessesSynced;
    private boolean isCacheUsed;
    private WorkerCache cache;

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

    public boolean doesAgentExistInThisCore(String agentName) {
        return localAgentStore.doesAgentExist(agentName);
    }

    public Agent getAgentByName(String targetAgentName) {
        if (doesAgentExistInThisCore(targetAgentName))
            return localAgentStore.get(targetAgentName);

        if (isCacheUsed && cache.doesAgentExist(targetAgentName))
            return cache.getAgent(targetAgentName);

        if (!areProcessesSynced)
            return null;

        try {
            Agent requestedAgent = requestResponseOperator.getAgentFromCoordinator(agent.name(), targetAgentName);
            if (isCacheUsed)
                cache.addAgent(requestedAgent);
            return requestedAgent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Agent> getFilteredAgents(Predicate<Agent> agentFilter) {
        if (isCacheUsed && cache.doesAgentFilterExist(agentFilter))
            return cache.getFilteredAgents(agentFilter);

        List<Agent> filteredAgents;
        if (areProcessesSynced) {
            try {
                filteredAgents = requestResponseOperator.getFilteredAgentsFromCoordinator(agent.name(), agentFilter);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            filteredAgents = localAgentStore.getFilteredAgents(agentFilter);

        if (isCacheUsed) {
            cache.addAgentFilter(agentFilter);
            cache.addAgents(filteredAgents);
        }

        return filteredAgents;
    }

    public ModelAttributeSet getModelAttributeSet(String attributeName) {
        if (isCacheUsed && cache.doModelAttributeSetsExist())
            return cache.getModelAttributeSet(attributeName);

        ModelAttributeSet requestedModelAttributeSet;
        if (areProcessesSynced) {
            try {
                Map<String, ModelAttributeSet> modelAttributeSets = requestResponseOperator.getModelAttributesFromCoordinator(agent.name());
                requestedModelAttributeSet = modelAttributeSets.get(attributeName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            requestedModelAttributeSet = modelAttributeSetMap.get(attributeName);

        if (isCacheUsed)
            cache.setModelAttributeSet(requestedModelAttributeSet);

        return requestedModelAttributeSet;
    }
}
