package agents.attributes.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AgentProperties {
    private final Map<String, AgentProperty<?>> propertiesMap = new HashMap<String, AgentProperty<?>>();
    private final List<AgentProperty<?>> propertiesList = new ArrayList<AgentProperty<?>>();

    public <T> void addProperty(AgentProperty<T> newProperty) {
        propertiesMap.put(newProperty.name(), newProperty);
        propertiesList.add(newProperty);
    }

    public void addProperties(AgentProperty<?>[] newProperties) {
        for (AgentProperty<?> newProperty : newProperties) {
            addProperty(newProperty);
        }
    }

    public AgentProperty<?> getProperty(String name) {
        return propertiesMap.get(name);
    }

    public AgentProperty<?> getPropertyByIndex(int index) {
        return propertiesList.get(index);
    }

    public int getPropertyCount() {
        return propertiesList.size();
    }

    public void run() {
        for (AgentProperty<?> property : propertiesList) {
            property.run();
        }
    }

    public void forEach(Consumer<AgentProperty<?>> action) {
        for (AgentProperty<?> property : propertiesList) {
            action.accept(property);
        }
    }

    public List<String> getPropertyNames() {
        return new ArrayList<>(propertiesMap.keySet());
    }

    public Object getPropertyValue(String propertyName) {
        AgentProperty<?> property = propertiesMap.get(propertyName);
        if (property == null)
            return null;
        return property.get();
    }
}
