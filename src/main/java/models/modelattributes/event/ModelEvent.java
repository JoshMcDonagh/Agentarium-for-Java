package models.modelattributes.event;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.List;
import java.util.Map;

public abstract class ModelEvent {
    private static int modelEventCount = 0;

    private final String name;
    private final boolean isRecorded;

    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private AgentStore agentStore;

    public ModelEvent(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        modelEventCount++;
    }

    public ModelEvent(boolean isRecorded) {
        this("Property Event " + modelEventCount, isRecorded);
    }

    public ModelEvent(String name) {
        this(name, true);
    }

    public ModelEvent() {
        this("Property Event " + modelEventCount, true);
    }

    public String name() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    void setModelAttributeSets(Map<String, ModelAttributeSet> modelAttributeSetList) {
        this.modelAttributeSetMap = modelAttributeSetList;
    }

    void setAgentStore(AgentStore agentStore) {
        this.agentStore = agentStore;
    }

    protected ModelAttributeSet getModelAttributeSet(String attributeName) {
        return modelAttributeSetMap.get(attributeName);
    }

    protected AgentStore getAgentStore() {
        return agentStore;
    }

    public abstract boolean isTriggered();
    public abstract void run();
}
