package agentarium.multithreading.utils;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.AttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class WorkerCache {
    private final List<Predicate<Agent>> agentFiltersCacheList = new ArrayList<>();
    private final Map<String, AttributeSet> environmentAttributeSetMap = new HashMap<>();
    private final AgentSet agentSetCache;

    public WorkerCache(boolean isAgentCopiesHeld) {
        agentSetCache = new AgentSet(isAgentCopiesHeld);
    }

    public void clear() {
        agentFiltersCacheList.clear();
        environmentAttributeSetMap.clear();
        agentSetCache.clear();
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

    public boolean doEnvironmentAttributeSetsExist() {
        return !environmentAttributeSetMap.isEmpty();
    }

    public void setEnvironmentAttributeSet(AttributeSet environmentAttributeSet) {
        environmentAttributeSetMap.put(environmentAttributeSet.getName(), environmentAttributeSet);
    }

    public AttributeSet getEnvironmentAttributeSet(String attributeSetName) {
        return environmentAttributeSetMap.get(attributeSetName);
    }
}
