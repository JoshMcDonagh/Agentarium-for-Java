package models.modelattributes.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelProperties {
    private final Map<String, ModelProperty<?>> propertiesMap = new HashMap<String, ModelProperty<?>>();
    private final List<ModelProperty<?>> propertiesList = new ArrayList<ModelProperty<?>>();

    public <T> void addProperty(ModelProperty<T> property) {
        propertiesMap.put(property.name(), property);
        propertiesList.add(property);
    }

    public void addProperties(ModelProperty<?> properties) {
        for (ModelProperty<?> property : propertiesList)
            addProperty(property);
    }

    public ModelProperty<?> getProperty(String name) {
        return propertiesMap.get(name);
    }

    public ModelProperty<?> getPropertyByIndex(int index) {
        return propertiesList.get(index);
    }

    public int getPropertyCount() {
        return propertiesList.size();
    }

    public void run() {
        for (ModelProperty<?> property : propertiesList)
            property.run();
    }
}
