package models.modelattributes.property;

import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.Map;

public abstract class ModelProperty<T> {
    private static int modelPropertyCount = 0;

    private final String name;
    private final boolean isRecorded;

    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private AgentStore agentStore;

    public ModelProperty(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        modelPropertyCount++;
    }

    public ModelProperty(boolean isRecorded) {
        this("Model Property " + modelPropertyCount, isRecorded);
    }

    public ModelProperty(String name) {
        this(name, true);
    }

    public ModelProperty() {
        this("Model Property " + modelPropertyCount, true);
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

    public Class<T> type() {
        return (Class<T>) getClass();
    }

    public abstract void set(T value);
    public abstract T get();
    public abstract void run();
}
