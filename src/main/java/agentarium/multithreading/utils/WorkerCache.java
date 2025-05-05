package agentarium.multithreading.utils;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.AttributeSet;
import agentarium.environments.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class WorkerCache {
    private final List<Predicate<Agent>> agentFiltersCacheList = new ArrayList<>();
    private final AgentSet agentSetCache;

    private Environment environment = null;

    public WorkerCache(boolean isAgentCopiesHeld) {
        agentSetCache = new AgentSet(isAgentCopiesHeld);
    }

    public void clear() {
        agentFiltersCacheList.clear();
        agentSetCache.clear();
        environment = null;
    }

    public boolean doesAgentFilterExist(Predicate<Agent> agentFilter) {
        return agentFiltersCacheList.contains(agentFilter);
    }

    public void addAgentFilter(Predicate<Agent> agentFilter) {
        agentFiltersCacheList.add(agentFilter);
    }

    public AgentSet getFilteredAgents(Predicate<Agent> agentFilter) {
        return agentSetCache.getFilteredAgents(agentFilter);
    }

    public boolean doesAgentExist(String agentName) {
        return agentSetCache.doesAgentExist(agentName);
    }

    public Agent getAgent(String agentName) {
        return agentSetCache.get(agentName);
    }

    public void addAgent(Agent agent) {
        agentSetCache.add(agent);
    }

    public void addAgents(AgentSet agentSet) {
        agentSetCache.add(agentSet);
    }

    public boolean doesEnvironmentExist() {
        return environment != null;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void addEnvironment(Environment environment) {
        this.environment = environment;
    }
}
