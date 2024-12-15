package attributedatabases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link AttributeResultsDatabase} that stores attribute results in memory.
 * This class is useful for scenarios where persistence is not required and the data can be held in memory for the
 * duration of the application's runtime.
 */
public class MemoryBasedDatabase extends AttributeResultsDatabase {

    // Maps to store property values and their associated classes
    private final Map<String, List<Object>> propertiesMap = new HashMap<>();
    private final Map<String, Class<?>> propertyClassesMap = new HashMap<>();

    // Maps to store pre-event values and their associated classes
    private final Map<String, List<Object>> preEventsMap = new HashMap<>();
    private final Map<String, Class<?>> preEventClassesMap = new HashMap<>();

    // Maps to store post-event values and their associated classes
    private final Map<String, List<Object>> postEventsMap = new HashMap<>();
    private final Map<String, Class<?>> postEventClassesMap = new HashMap<>();

    /**
     * Adds a property value to the in-memory storage.
     *
     * @param propertyName  Name of the property.
     * @param propertyValue Value of the property.
     * @param <T>           Type of the property value.
     * @throws IllegalArgumentException If the property value type does not match the existing type.
     */
    @Override
    public <T> void addPropertyValue(String propertyName, T propertyValue) {
        if (!propertiesMap.containsKey(propertyName)) {
            propertiesMap.put(propertyName, new ArrayList<>());
            propertyClassesMap.put(propertyName, propertyValue.getClass());
        }

        if (propertyClassesMap.get(propertyName).isInstance(propertyValue)) {
            propertiesMap.get(propertyName).add(propertyValue);
        } else {
            throw new IllegalArgumentException("Property " + propertyName + " is not an instance of " + propertyValue.getClass());
        }
    }

    /**
     * Adds a pre-event value to the in-memory storage.
     *
     * @param preEventName  Name of the pre-event.
     * @param preEventValue Value of the pre-event.
     * @param <T>           Type of the pre-event value.
     * @throws IllegalArgumentException If the pre-event value type does not match the existing type.
     */
    @Override
    public <T> void addPreEventValue(String preEventName, T preEventValue) {
        if (!preEventsMap.containsKey(preEventName)) {
            preEventsMap.put(preEventName, new ArrayList<>());
            preEventClassesMap.put(preEventName, preEventValue.getClass());
        }

        if (preEventClassesMap.get(preEventName).isInstance(preEventValue)) {
            preEventsMap.get(preEventName).add(preEventValue);
        } else {
            throw new IllegalArgumentException("Pre-Event " + preEventName + " is not an instance of " + preEventValue.getClass());
        }
    }

    /**
     * Adds a post-event value to the in-memory storage.
     *
     * @param postEventName  Name of the post-event.
     * @param postEventValue Value of the post-event.
     * @param <T>            Type of the post-event value.
     * @throws IllegalArgumentException If the post-event value type does not match the existing type.
     */
    @Override
    public <T> void addPostEventValue(String postEventName, T postEventValue) {
        if (!postEventsMap.containsKey(postEventName)) {
            postEventsMap.put(postEventName, new ArrayList<>());
            postEventClassesMap.put(postEventName, postEventValue.getClass());
        }

        if (postEventClassesMap.get(postEventName).isInstance(postEventValue)) {
            postEventsMap.get(postEventName).add(postEventValue);
        } else {
            throw new IllegalArgumentException("Post-Event " + postEventName + " is not an instance of " + postEventValue.getClass());
        }
    }

    /**
     * Replaces all values for a specific property.
     *
     * @param propertyName  Name of the property.
     * @param propertyValues New values to replace the existing ones.
     * @throws IllegalArgumentException If the property does not exist.
     */
    @Override
    public void replacePropertyColumn(String propertyName, List<Object> propertyValues) {
        if (propertiesMap.containsKey(propertyName)) {
            propertyClassesMap.put(propertyName, propertyValues.get(0).getClass());
            propertiesMap.put(propertyName, propertyValues);
        } else {
            throw new IllegalArgumentException("Property " + propertyName + " does not exist and cannot be replaced.");
        }
    }

    /**
     * Replaces all values for a specific pre-event.
     *
     * @param preEventName  Name of the pre-event.
     * @param preEventValues New values to replace the existing ones.
     * @throws IllegalArgumentException If the pre-event does not exist.
     */
    @Override
    public void replacePreEventTrigger(String preEventName, List<Object> preEventValues) {
        if (preEventsMap.containsKey(preEventName)) {
            preEventClassesMap.put(preEventName, preEventValues.get(0).getClass());
            preEventsMap.put(preEventName, preEventValues);
        } else {
            throw new IllegalArgumentException("Pre-Event " + preEventName + " does not exist and cannot be replaced.");
        }
    }

    /**
     * Replaces all values for a specific post-event.
     *
     * @param postEventName  Name of the post-event.
     * @param postEventValues New values to replace the existing ones.
     * @throws IllegalArgumentException If the post-event does not exist.
     */
    @Override
    public void replacePostEventTrigger(String postEventName, List<Object> postEventValues) {
        if (postEventsMap.containsKey(postEventName)) {
            postEventClassesMap.put(postEventName, postEventValues.get(0).getClass());
            postEventsMap.put(postEventName, postEventValues);
        } else {
            throw new IllegalArgumentException("Post-Event " + postEventName + " does not exist and cannot be replaced.");
        }
    }

    /**
     * Retrieves all values for a specific property.
     *
     * @param propertyName Name of the property.
     * @return List of property values.
     */
    @Override
    public List<Object> getPropertyColumnAsList(String propertyName) {
        return propertiesMap.get(propertyName);
    }

    /**
     * Retrieves all values for a specific pre-event.
     *
     * @param preEventName Name of the pre-event.
     * @return List of pre-event values.
     */
    @Override
    public List<Object> getPreEventColumnAsList(String preEventName) {
        return preEventsMap.get(preEventName);
    }

    /**
     * Retrieves all values for a specific post-event.
     *
     * @param postEventName Name of the post-event.
     * @return List of post-event values.
     */
    @Override
    public List<Object> getPostEventColumnAsList(String postEventName) {
        return postEventsMap.get(postEventName);
    }
}
