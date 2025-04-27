package agentarium.agents;

import java.util.*;
import java.util.function.Predicate;

public class AgentSet implements Iterable<Agent> {
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
        int index;

        if (doesAgentExist(agent.getName()))
            index = agentIndexes.get(agent.getName());
        else {
            index = agents.size();
            agentIndexes.put(agent.getName(), index);
        }

        if (isStoringAgentCopies)
            agents.set(index, agent.deepCopyDuplicate());
        else
            agents.set(index, agent);
    }

    public void add(List<Agent> agents) {
        for (Agent agent : agents)
            add(agent);
    }

    public void add(AgentSet agentSet) {
        add(agentSet.getAsList());
    }

    public Agent get(String agentName) {
        int index = agentIndexes.get(agentName);
        return agents.get(index);
    }

    public Agent get(int index) {
        return agents.get(index);
    }

    public List<Agent> getAsList() {
        return agents;
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

    public Iterator<Agent> getRandomIterator() {
        List<Agent> shuffledAgents = new ArrayList<>(agents);
        Collections.shuffle(shuffledAgents);
        return shuffledAgents.iterator();
    }

    @Override
    public Iterator<Agent> iterator() {
        return agents.iterator();
    }
}
