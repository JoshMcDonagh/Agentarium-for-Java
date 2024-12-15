package models.modelattributes.property;

import agents.attributes.property.AgentProperty;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelProperties {
    private final Map<String, ModelProperty<?>> propertiesMap = new HashMap<String, ModelProperty<?>>();
    private final List<ModelProperty<?>> propertiesList = new ArrayList<ModelProperty<?>>();

    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private AgentStore agentStore;

    public void setModelAttributeSets(Map<String, ModelAttributeSet> modelAttributeSetList) {
        this.modelAttributeSetMap = modelAttributeSetList;
    }

    public void setAgentStore(AgentStore agentStore) {
        this.agentStore = agentStore;
    }

    public <T> void addProperty(ModelProperty<T> newProperty) {
        newProperty.setModelAttributeSets(modelAttributeSetMap);
        newProperty.setAgentStore(agentStore);
        propertiesMap.put(newProperty.name(), newProperty);
        propertiesList.add(newProperty);
    }

    public void addProperties(List<ModelProperty<?>> newProperties) {
        for (ModelProperty<?> property : newProperties)
            addProperty(property);
    }

    public ModelProperty<?> getProperty(String name) {
        return propertiesMap.get(name);
    }

    public ModelProperty<?> getPropertyByIndex(int index) {
        return propertiesList.get(index);
    }

    public List<ModelProperty<?>> getPropertiesList() {
        return propertiesList;
    }

    public int getPropertyCount() {
        return propertiesList.size();
    }

    public void run() {
        for (ModelProperty<?> property : propertiesList)
            property.run();
    }
}
