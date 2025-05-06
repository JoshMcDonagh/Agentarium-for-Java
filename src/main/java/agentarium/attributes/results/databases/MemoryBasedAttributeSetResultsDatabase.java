package agentarium.attributes.results.databases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An in-memory implementation of {@link AttributeSetResultsDatabase}.
 *
 * <p>This class stores all simulation results directly in RAM, using Java collections.
 * It is useful for lightweight simulations, unit tests, or post-processing before output.
 *
 * <p>Unlike {@link DiskBasedAttributeSetResultsDatabase}, this class does not persist results to disk.
 */
public class MemoryBasedAttributeSetResultsDatabase extends AttributeSetResultsDatabase {

    // === Internal maps to store values by name ===

    private final Map<String, List<Object>> propertiesMap = new HashMap<>();
    private final Map<String, Class<?>> propertyClassesMap = new HashMap<>();

    private final Map<String, List<Object>> preEventsMap = new HashMap<>();
    private final Map<String, Class<?>> preEventClassesMap = new HashMap<>();

    private final Map<String, List<Object>> postEventsMap = new HashMap<>();
    private final Map<String, Class<?>> postEventClassesMap = new HashMap<>();

    /**
     * Overrides setDatabasePath but ignores the value, as memory-based storage does not require a file path.
     *
     * @param databasePath ignored
     */
    @Override
    protected void setDatabasePath(String databasePath) {
        // No-op for in-memory implementation
        return;
    }

    // === Tick-by-tick value addition ===

    @Override
    public <T> void addPropertyValue(String propertyName, T propertyValue) {
        if (!propertiesMap.containsKey(propertyName)) {
            propertiesMap.put(propertyName, new ArrayList<>());
            propertyClassesMap.put(propertyName, propertyValue.getClass());
        }

        if (propertyClassesMap.get(propertyName).isInstance(propertyValue))
            propertiesMap.get(propertyName).add(propertyValue);
        else
            throw new IllegalArgumentException("Property '" + propertyName + "' is not an instance of " + propertyValue.getClass().getSimpleName());
    }

    @Override
    public <T> void addPreEventValue(String preEventName, T preEventValue) {
        if (!preEventsMap.containsKey(preEventName)) {
            preEventsMap.put(preEventName, new ArrayList<>());
            preEventClassesMap.put(preEventName, preEventValue.getClass());
        }

        if (preEventClassesMap.get(preEventName).isInstance(preEventValue))
            preEventsMap.get(preEventName).add(preEventValue);
        else
            throw new IllegalArgumentException("Pre-event '" + preEventName + "' is not an instance of " + preEventValue.getClass().getSimpleName());
    }

    @Override
    public <T> void addPostEventValue(String postEventName, T postEventValue) {
        if (!postEventsMap.containsKey(postEventName)) {
            postEventsMap.put(postEventName, new ArrayList<>());
            postEventClassesMap.put(postEventName, postEventValue.getClass());
        }

        if (postEventClassesMap.get(postEventName).isInstance(postEventValue))
            postEventsMap.get(postEventName).add(postEventValue);
        else
            throw new IllegalArgumentException("Post-event '" + postEventName + "' is not an instance of " + postEventValue.getClass().getSimpleName());
    }

    // === Column replacement ===

    @Override
    public void setPropertyColumn(String propertyName, List<Object> propertyValues) {
        if (propertiesMap.containsKey(propertyName)) {
            propertyClassesMap.put(propertyName, propertyValues.get(0).getClass());
            propertiesMap.put(propertyName, propertyValues);
        } else {
            throw new IllegalArgumentException("Property '" + propertyName + "' does not exist and cannot be replaced.");
        }
    }

    @Override
    public void setPreEventColumn(String preEventName, List<Object> preEventValues) {
        if (preEventsMap.containsKey(preEventName)) {
            preEventClassesMap.put(preEventName, preEventValues.get(0).getClass());
            preEventsMap.put(preEventName, preEventValues);
        } else {
            throw new IllegalArgumentException("Pre-event '" + preEventName + "' does not exist and cannot be replaced.");
        }
    }

    @Override
    public void setPostEventColumn(String postEventName, List<Object> postEventValues) {
        if (postEventsMap.containsKey(postEventName)) {
            postEventClassesMap.put(postEventName, postEventValues.get(0).getClass());
            postEventsMap.put(postEventName, postEventValues);
        } else {
            throw new IllegalArgumentException("Post-event '" + postEventName + "' does not exist and cannot be replaced.");
        }
    }

    // === Column retrieval ===

    @Override
    public List<Object> getPropertyColumnAsList(String propertyName) {
        return propertiesMap.get(propertyName);
    }

    @Override
    public List<Object> getPreEventColumnAsList(String preEventName) {
        return preEventsMap.get(preEventName);
    }

    @Override
    public List<Object> getPostEventColumnAsList(String postEventName) {
        return postEventsMap.get(postEventName);
    }
}
