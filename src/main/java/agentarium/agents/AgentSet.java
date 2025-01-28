package agentarium.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AgentSet {
    private boolean isStoringAgentCopies;
    private Map<String, Integer> agentIndexes;
    private List<Agent> agents;

    public AgentSet(boolean isStoringAgentCopies) {
        this.isStoringAgentCopies = isStoringAgentCopies;
        agentIndexes = new HashMap<>();
        agents = new ArrayList<>();
    }

    public AgentSet(List<Agent> agentsList, boolean isStoringAgentCopies) {
        this(isStoringAgentCopies);
        add(agentsList);
    }

    public AgentSet(List<Agent> agentsList) {
        this();
        add(agentsList);
    }

    public AgentSet() {
        this(false);
    }

    public void setIsStoringAgentCopiesFlag(boolean isStoringAgentCopies) {
        this.isStoringAgentCopies = isStoringAgentCopies;
    }

    public void add(Agent agent) {
        if (doesAgentExist(agent.getName())) {
            int index = agentIndexes.get(agent.getName());
            agents.set(index, agent);
        } else {
            int index = agents.size();
            agentIndexes.put(agent.getName(), index);
            agents.add(agent);
        }
    }

    public void add(List<Agent> agents) {
        for (Agent agent : agents)
            add(agent);
    }

    public Agent get(String agentName) {
        int index = agentIndexes.get(agentName);
        return agents.get(index);
    }

    public Agent get(int index) {
        return agents.get(index);
    }

    public int size() {
        return agents.size();
    }

    public void clear() {
        agentIndexes = new HashMap<>();
        agents = new ArrayList<>();
    }

    public boolean doesAgentExist(String agentName) {
        return agentIndexes.containsKey(agentName);
    }

    public void update(AgentSet otherAgentSet) {
        for (int i = 0; i < otherAgentSet.size(); i++) {
            Agent agent = otherAgentSet.get(i);
            add(agent);
        }
    }

    public AgentSet getFilteredAgents(Predicate<Agent> agentFilter) {
        List<Agent> filteredAgents = new ArrayList<>();

        for (Agent agent : agents) {
            if (agentFilter.test(agent))
                filteredAgents.add(agent);
        }

        return new AgentSet(filteredAgents);
    }
}
