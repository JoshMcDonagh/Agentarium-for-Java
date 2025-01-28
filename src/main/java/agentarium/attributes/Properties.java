package agentarium.attributes;

import java.util.List;
import java.util.Map;

public class Properties {
    private Map<String, Integer> propertyIndexes;
    private List<Property<?>> properties;

    public <T> void add(Property<T> property) {
        Integer index = properties.size();
        propertyIndexes.put(property.getName(), index);
        properties.add(property);
    }

    public void add(List<Property<?>> properties) {
        for (Property<?> property : properties)
            add(property);
    }

    public Property<?> getProperty(String name) {
        int index = propertyIndexes.get(name);
        return properties.get(index);
    }

    public Property<?> getProperty(int index) {
        return properties.get(index);
    }

    public int size() {
        return properties.size();
    }

    public void run() {
        for (Property<?> property : properties)
            property.run();
    }
}
