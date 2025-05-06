package agentarium.agents;

import java.util.*;
import java.util.function.Predicate;

/**
 * A collection class for managing {@link Agent} instances, with support for:
 * <ul>
 *     <li>Optional deep copying of agents on insertion</li>
 *     <li>Fast lookup by agent name</li>
 *     <li>Filtering, duplication, and setup routines</li>
 *     <li>Randomised iteration</li>
 * </ul>
 *
 * <p>This class is iterable and designed to support both sequential and parallel simulation use cases.
 */
public class AgentSet implements Iterable<Agent> {

    /** Whether to store deep copies of added agents rather than references */
    private boolean isStoringAgentCopies;

    /** Map from agent names to their index in the list */
    private Map<String, Integer> agentIndexes;

    /** Ordered list of agents in the set */
    private List<Agent> agents;

    /**
     * Constructs an empty agent set with optional deep copy behaviour.
     *
     * @param isStoringAgentCopies whether added agents should be deep copied
     */
    public AgentSet(boolean isStoringAgentCopies) {
        this.isStoringAgentCopies = isStoringAgentCopies;
        agentIndexes = new HashMap<>();
        agents = new ArrayList<>();
    }

    /**
     * Constructs a new agent set from a list of agents, with optional deep copying.
     *
     * @param agentsList list of agents to add
     * @param isStoringAgentCopies whether to store deep copies
     */
    public AgentSet(List<Agent> agentsList, boolean isStoringAgentCopies) {
        this(isStoringAgentCopies);
        add(agentsList);
    }

    /**
     * Constructs a new agent set from a list of agents, without deep copying.
     *
     * @param agentsList list of agents to add
     */
    public AgentSet(List<Agent> agentsList) {
        this();
        add(agentsList);
    }

    /** Constructs an empty agent set without deep copying. */
    public AgentSet() {
        this(false);
    }

    /**
     * Updates the internal flag for whether agent copies should be stored.
     *
     * @param isStoringAgentCopies true to enable deep copy on addition
     */
    public void setIsStoringAgentCopiesFlag(boolean isStoringAgentCopies) {
        this.isStoringAgentCopies = isStoringAgentCopies;
    }

    /**
     * Adds an agent to the set. If the agent already exists, it will be replaced.
     *
     * @param agent the agent to add
     */
    public void add(Agent agent) {
        int index;

        if (doesAgentExist(agent.getName())) {
            index = agentIndexes.get(agent.getName());
        } else {
            index = agents.size();
            agentIndexes.put(agent.getName(), index);
            agents.add(null); // Ensure list is long enough before setting
        }

        if (isStoringAgentCopies)
            agents.set(index, agent.deepCopyDuplicate());
        else
            agents.set(index, agent);
    }

    /**
     * Adds a list of agents to the set.
     *
     * @param agents list of agents to add
     */
    public void add(List<Agent> agents) {
        for (Agent agent : agents)
            add(agent);
    }

    /**
     * Adds all agents from another {@link AgentSet}.
     *
     * @param agentSet the agent set to add from
     */
    public void add(AgentSet agentSet) {
        add(agentSet.getAsList());
    }

    /**
     * Retrieves an agent by name.
     *
     * @param agentName the agent's unique name
     * @return the agent instance
     */
    public Agent get(String agentName) {
        int index = agentIndexes.get(agentName);
        return agents.get(index);
    }

    /**
     * Retrieves an agent by index.
     *
     * @param index the index of the agent
     * @return the agent at the given position
     */
    public Agent get(int index) {
        return agents.get(index);
    }

    /**
     * Returns the list of agents in this set.
     *
     * @return a list of agent instances
     */
    public List<Agent> getAsList() {
        return agents;
    }

    /**
     * Returns the number of agents in the set.
     *
     * @return the size of the agent set
     */
    public int size() {
        return agents.size();
    }

    /**
     * Clears the agent set entirely.
     */
    public void clear() {
        agentIndexes = new HashMap<>();
        agents = new ArrayList<>();
    }

    /**
     * Checks if an agent exists in the set by name.
     *
     * @param agentName the name to check
     * @return true if the agent exists
     */
    public boolean doesAgentExist(String agentName) {
        return agentIndexes.containsKey(agentName);
    }

    /**
     * Updates this set with all agents from another set.
     * Existing agents are replaced if names match.
     *
     * @param otherAgentSet the other agent set to pull from
     */
    public void update(AgentSet otherAgentSet) {
        for (int i = 0; i < otherAgentSet.size(); i++) {
            Agent agent = otherAgentSet.get(i);
            add(agent);
        }
    }

    /**
     * Returns a filtered view of the agent set.
     *
     * @param agentFilter a predicate to apply to each agent
     * @return a new {@code AgentSet} containing only matching agents
     */
    public AgentSet getFilteredAgents(Predicate<Agent> agentFilter) {
        List<Agent> filteredAgents = new ArrayList<>();

        for (Agent agent : agents) {
            if (agentFilter.test(agent))
                filteredAgents.add(agent);
        }

        return new AgentSet(filteredAgents);
    }

    /**
     * Returns a randomised iterator over the agents in this set.
     *
     * @return an iterator that yields agents in random order
     */
    public Iterator<Agent> getRandomIterator() {
        List<Agent> shuffledAgents = new ArrayList<>(agents);
        Collections.shuffle(shuffledAgents);
        return shuffledAgents.iterator();
    }

    /**
     * Calls {@code setup()} on all agents in the set.
     * Should be called before the simulation begins.
     */
    public void setup() {
        for (Agent agent : agents)
            agent.setup();
    }

    /**
     * Returns a duplicate of this agent set.
     * If deep copy is enabled, agents will be duplicated as well.
     *
     * @return a new {@code AgentSet} with the same agents
     */
    public AgentSet duplicate() {
        return new AgentSet(agents, isStoringAgentCopies);
    }

    /**
     * Standard iterator over the agents in the order they were added.
     *
     * @return an iterator over the agent list
     */
    @Override
    public Iterator<Agent> iterator() {
        return agents.iterator();
    }
}
