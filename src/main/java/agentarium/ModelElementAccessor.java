package agentarium;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.multithreading.requestresponse.RequestResponseInterface;
import agentarium.multithreading.utils.WorkerCache;

import java.util.function.Predicate;

public class ModelElementAccessor {
    private ModelElement modelElement;
    private AgentSet localAgentSet;
    private ModelSettings settings;
    private WorkerCache cache;
    private RequestResponseInterface requestResponseInterface;
    private Environment localEnvironment;

    public ModelElementAccessor(
            ModelElement modelElement,
            AgentSet localAgentSet,
            ModelSettings settings,
            WorkerCache cache,
            RequestResponseInterface requestResponseInterface,
            Environment localEnvironment
    ) {
        this.modelElement = modelElement;
        this.localAgentSet = localAgentSet;
        this.settings = settings;
        this.cache = cache;
        this.requestResponseInterface = requestResponseInterface;
        this.localEnvironment = localEnvironment;
    }

    public boolean doesAgentExistInThisCore(String agentName) {
        return localAgentSet.doesAgentExist(agentName);
    }

    public Agent getAgentByName(String targetAgentName) {
        if (doesAgentExistInThisCore(targetAgentName))
            return localAgentSet.get(targetAgentName);

        if (settings.getIsCacheUsed() && cache.doesAgentExist(targetAgentName))
            return cache.getAgent(targetAgentName);

        if (!settings.getAreProcessesSynced())
            return null;

        try {
            Agent requestedAgent = requestResponseInterface.getAgentFromCoordinator(modelElement.getName(), targetAgentName);
            if (settings.getIsCacheUsed())
                cache.addAgent(requestedAgent);
            return requestedAgent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AgentSet getFilteredAgents(Predicate<Agent> filter) {
        if (settings.getIsCacheUsed() && cache.doesAgentFilterExist(filter))
            return cache.getFilteredAgents(filter);

        AgentSet filteredAgentSet;
        if (settings.getAreProcessesSynced()) {
            try {
                filteredAgentSet = requestResponseInterface.getFilteredAgentsFromCoordinator(modelElement.getName(), filter);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            filteredAgentSet = localAgentSet.getFilteredAgents(filter);

        if (settings.getIsCacheUsed()) {
            cache.addAgentFilter(filter);
            cache.addAgents(filteredAgentSet);
        }

        return filteredAgentSet;
    }

    public Environment getEnvironment() {
        if (!settings.getAreProcessesSynced())
            return localEnvironment;

        if (settings.getIsCacheUsed() && cache.doesEnvironmentExist())
            return cache.getEnvironment();

        Environment requestedEnvironment;

        try {
            requestedEnvironment = requestResponseInterface.getEnvironmentFromCoordinator(modelElement.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (settings.getIsCacheUsed())
            cache.addEnvironment(requestedEnvironment);

        return requestedEnvironment;
    }
}
