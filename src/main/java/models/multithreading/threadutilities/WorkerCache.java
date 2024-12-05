package models.multithreading.threadutilities;

import agents.Agent;
import models.modelattributes.ModelAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class WorkerCache {
    private final List<Predicate<Agent>> agentFiltersCacheList = new ArrayList<Predicate<Agent>>();
    private final Map<String, ModelAttributeSet> modelAttributeSetsCache = new HashMap<String, ModelAttributeSet>();
    private final AgentStore agentStoreCache;

    public WorkerCache(boolean isAgentCopiesHeld) {
        agentStoreCache = new AgentStore(isAgentCopiesHeld);
    }

    public void clear() {
        agentFiltersCacheList.clear();
        modelAttributeSetsCache.clear();
        agentStoreCache.clear();
    }

    public boolean doesAgentFilterExist(Predicate<Agent> agentFilter) {
        return agentFiltersCacheList.contains(agentFilter);
    }

    public void addAgentFilter(Predicate<Agent> agentFilter) {
        agentFiltersCacheList.add(agentFilter);
    }

    public List<Agent> getFilteredAgents(Predicate<Agent> agentFilter) {
        return agentStoreCache.getFilteredAgents(agentFilter);
    }

    public boolean doesAgentExist(String agentName) {
        return agentStoreCache.doesAgentExist(agentName);
    }

    public Agent getAgent(String agentName) {
        return agentStoreCache.get(agentName);
    }

    public void addAgent(Agent agent) {
        agentStoreCache.addAgent(agent);
    }

    public void addAgents(List<Agent> agents) {
        agentStoreCache.addAgents(agents);
    }

    public boolean doModelAttributeSetsExist() {
        return !modelAttributeSetsCache.isEmpty();
    }

    public ModelAttributeSet getModelAttributeSet(String attributeSetName) {
        return modelAttributeSetsCache.get(attributeSetName);
    }
}
