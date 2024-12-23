package models.multithreading.threadutilities;

import agents.Agent;
import models.results.FinalAgentAttributeResults;
import models.results.Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AgentStore {
    private final boolean isStoringAgentCopies;
    private List<Agent> agentsList = new ArrayList<Agent>();
    private Map<String, Agent> agentsMap = new HashMap<String, Agent>();

    public AgentStore(boolean isStoringAgentCopies) {
        this.isStoringAgentCopies = isStoringAgentCopies;
    }

    public AgentStore() {
        isStoringAgentCopies = true;
    }

    public void clear() {
        agentsMap = new HashMap<String, Agent>();
    }

    public void addAgent(Agent agent) {
        Agent agentToStore;

        if (isStoringAgentCopies) {
            agentToStore = agent.duplicateWithoutRecords();
        } else {
            agentToStore = agent;
        }

        agentsList.add(agentToStore);
        agentsMap.put(agentToStore.name(), agentToStore);
    }

    public void addAgents(List<Agent> agents) {
        for (Agent agent : agents)
            addAgent(agent);
    }

    public Agent get(String agentName) {
        return agentsMap.get(agentName);
    }

    public List<Agent> getAgentsList() {
        return agentsList;
    }

    public void update(AgentStore otherAgentStore) {
        for (Map.Entry<String, Agent> entry : otherAgentStore.agentsMap.entrySet())
            agentsMap.put(entry.getKey(),
                    isStoringAgentCopies ? entry.getValue().duplicateWithoutRecords() : entry.getValue());
    }

    public boolean doesAgentExist(String agentName) {
        return agentsMap.containsKey(agentName);
    }

    public List<Agent> getFilteredAgents(Predicate<Agent> agentFilter) {
        List<Agent> filteredAgents = new ArrayList<Agent>();

        for (Agent agent : agentsMap.values())
            if (agentFilter.test(agent))
                filteredAgents.add(agent);

        return filteredAgents;
    }
}
