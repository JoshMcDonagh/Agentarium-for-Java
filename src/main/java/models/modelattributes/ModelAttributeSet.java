package models.modelattributes;

import models.modelattributes.event.ModelEvents;
import models.modelattributes.property.ModelProperties;
import models.multithreading.threadutilities.AgentStore;

import java.util.Map;

public class ModelAttributeSet {
    private static int modelAttributeCount = 0;

    private ModelProperties properties;
    private ModelEvents preEvents;
    private ModelEvents postEvents;

    private final String name;

    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private AgentStore agentStore;

    public ModelAttributeSet(final String name) {
        this.name = name;
        modelAttributeCount++;
    }

    public ModelAttributeSet() {
        this("Model Attribute Set " + modelAttributeCount);
    }

    public void setModelAttributeSets(Map<String, ModelAttributeSet> modelAttributeSetList) {
        this.modelAttributeSetMap = modelAttributeSetList;
    }

    public void setAgentStore(AgentStore agentStore) {
        this.agentStore = agentStore;
        if (properties != null)
            properties.setAgentStore(agentStore);
        if (preEvents != null)
            preEvents.setAgentStore(agentStore);
        if (postEvents != null)
            postEvents.setAgentStore(agentStore);
    }

    public void setProperties(ModelProperties properties) {
        if (modelAttributeSetMap != null)
            properties.setModelAttributeSets(modelAttributeSetMap);
        if (agentStore != null)
            properties.setAgentStore(agentStore);
        this.properties = properties;
    }

    public void setPreEvents(ModelEvents preEvents) {
        if (modelAttributeSetMap != null)
            preEvents.setModelAttributeSets(modelAttributeSetMap);
        if (agentStore != null)
            preEvents.setAgentStore(agentStore);
        this.preEvents = preEvents;
    }

    public void setPostEvents(ModelEvents postEvents) {
        if (modelAttributeSetMap != null)
            postEvents.setModelAttributeSets(modelAttributeSetMap);
        if (agentStore != null)
            postEvents.setAgentStore(agentStore);
        this.postEvents = postEvents;
    }

    public String name() {
        return name;
    }

    public ModelProperties getProperties() {
        return properties;
    }

    public ModelEvents getPreEvents() {
        return preEvents;
    }

    public ModelEvents getPostEvents() {
        return postEvents;
    }

    public void run() {
        preEvents.run();
        properties.run();
        postEvents.run();
    }
}
