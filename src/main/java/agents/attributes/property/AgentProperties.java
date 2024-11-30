package agents.attributes.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
