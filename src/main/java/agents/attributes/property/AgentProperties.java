package agents.attributes.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A container for managing a collection of {@link AgentProperty} objects.
 * This class provides methods for adding, retrieving, running, and interacting with agent properties.
 */
public class AgentProperties {

    // A map for quick lookup of properties by their names.
    private final Map<String, AgentProperty<?>> propertiesMap = new HashMap<>();

    // A list for ordered access to properties.
    private final List<AgentProperty<?>> propertiesList = new ArrayList<>();

    /**
     * Adds a single property to the collection.
     *
     * @param newProperty The property to be added.
     * @param <T>         The type of the property's value.
     */
    public <T> void addProperty(AgentProperty<T> newProperty) {
        propertiesMap.put(newProperty.name(), newProperty); // Store the property in the map for quick lookup.
        propertiesList.add(newProperty); // Maintain the order of properties in the list.
    }

    /**
     * Adds multiple properties to the collection.
     *
     * @param newProperties An array of properties to be added.
     */
    public void addProperties(AgentProperty<?>[] newProperties) {
        for (AgentProperty<?> newProperty : newProperties) {
            addProperty(newProperty); // Reuse addProperty to ensure consistency.
        }
    }

    /**
     * Retrieves a property by its name.
     *
     * @param name The name of the property.
     * @return The property with the specified name, or {@code null} if no such property exists.
     */
    public AgentProperty<?> getProperty(String name) {
        return propertiesMap.get(name);
    }

    /**
     * Retrieves a property by its index in the ordered list.
     *
     * @param index The index of the property.
     * @return The property at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public AgentProperty<?> getPropertyByIndex(int index) {
        return propertiesList.get(index);
    }

    /**
     * Retrieves a list of all properties in the collection.
     *
     * @return A list of all properties.
     */
    public List<AgentProperty<?>> getPropertiesList() {
        return propertiesList;
    }

    /**
     * Retrieves the total number of properties in the collection.
     *
     * @return The number of properties.
     */
    public int getPropertyCount() {
        return propertiesList.size();
    }

    /**
     * Executes the {@code run()} method of each property in the collection.
     * This is intended to perform operations or calculations associated with each property.
     */
    public void run() {
        for (AgentProperty<?> property : propertiesList) {
            property.run();
        }
    }

    /**
     * Applies a specified action to each property in the collection.
     *
     * @param action A {@link Consumer} representing the action to be performed on each property.
     */
    public void forEach(Consumer<AgentProperty<?>> action) {
        for (AgentProperty<?> property : propertiesList) {
            action.accept(property);
        }
    }

    /**
     * Retrieves a list of all property names in the collection.
     *
     * @return A list of property names.
     */
    public List<String> getPropertyNames() {
        return new ArrayList<>(propertiesMap.keySet());
    }

    /**
     * Retrieves the value of a specific property by its name.
     *
     * @param propertyName The name of the property.
     * @return The value of the property, or {@code null} if the property does not exist.
     */
    public Object getPropertyValue(String propertyName) {
        AgentProperty<?> property = propertiesMap.get(propertyName);
        if (property == null) {
            return null; // Return null if the property is not found.
        }
        return property.get(); // Retrieve the property's value using its getter method.
    }
}
