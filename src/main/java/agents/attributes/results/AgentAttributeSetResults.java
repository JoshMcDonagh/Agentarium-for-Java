package agents.attributes.results;

import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;
import attributedatabases.AttributeResultsDatabase;
import attributedatabases.AttributeResultsDatabaseFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the results for a specific attribute set associated with an agent.
 * This class facilitates the recording and retrieval of properties and events (pre and post),
 * storing the data in an underlying database.
 */
public class AgentAttributeSetResults {

    // The name of the agent this attribute set belongs to.
    private final String agentName;

    // The name of the attribute set.
    private final String attributeName;

    // The database used for storing and managing results.
    private final AttributeResultsDatabase database;

    // A list of property names that are recorded.
    private final List<String> propertyNamesList = new ArrayList<>();

    // A map of property names to their respective data types.
    private final Map<String, Class<?>> propertyTypesMap = new HashMap<>();

    // A list of pre-event names that are recorded.
    private final List<String> preEventNamesList = new ArrayList<>();

    // A list of post-event names that are recorded.
    private final List<String> postEventNamesList = new ArrayList<>();

    /**
     * Constructs an `AgentAttributeSetResults` for a given agent and attribute set.
     * Initialises the database and records property and event names and types.
     *
     * @param agentName        The name of the agent.
     * @param agentAttributeSet The attribute set associated with the agent.
     */
    public AgentAttributeSetResults(String agentName, AgentAttributeSet agentAttributeSet) {
        this.agentName = agentName;
        this.attributeName = agentAttributeSet.name();
        this.database = AttributeResultsDatabaseFactory.createDatabase();
        assert database != null;
        database.connect();

        // Process and store properties that are marked as recorded.
        List<AgentProperty<?>> propertiesList = agentAttributeSet.getProperties().getPropertiesList();
        for (AgentProperty<?> property : propertiesList) {
            if (property.isRecorded()) {
                propertyNamesList.add(property.name());
                propertyTypesMap.put(property.name(), property.type());
            }
        }

        // Process and store pre-events that are marked as recorded.
        List<AgentEvent> preEventList = agentAttributeSet.getPreEvents().getEventsList();
        for (AgentEvent event : preEventList) {
            if (event.isRecorded()) {
                preEventNamesList.add(event.name());
            }
        }

        // Process and store post-events that are marked as recorded.
        List<AgentEvent> postEventList = agentAttributeSet.getPostEvents().getEventsList();
        for (AgentEvent event : postEventList) {
            if (event.isRecorded()) {
                postEventNamesList.add(event.name());
            }
        }
    }

    /**
     * Retrieves the name of the agent.
     *
     * @return The name of the agent.
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Retrieves the name of the attribute set.
     *
     * @return The name of the attribute set.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Retrieves a list of all recorded property names.
     *
     * @return A list of property names.
     */
    public List<String> getPropertyNamesList() {
        return propertyNamesList;
    }

    /**
     * Retrieves a list of all recorded pre-event names.
     *
     * @return A list of pre-event names.
     */
    public List<String> getPreEventNamesList() {
        return preEventNamesList;
    }

    /**
     * Retrieves a list of all recorded post-event names.
     *
     * @return A list of post-event names.
     */
    public List<String> getPostEventNamesList() {
        return postEventNamesList;
    }

    /**
     * Retrieves the data type of a specific property.
     *
     * @param propertyName The name of the property.
     * @return The data type of the property, or {@code null} if the property is not found.
     */
    public Class<?> getPropertyClass(String propertyName) {
        return propertyTypesMap.get(propertyName);
    }

    /**
     * Records a value for a specific property.
     *
     * @param propertyName The name of the property.
     * @param value        The value to record.
     */
    public void recordProperty(String propertyName, Object value) {
        database.addPropertyValue(propertyName, value);
    }

    /**
     * Records whether a specific pre-event was triggered.
     *
     * @param eventName  The name of the pre-event.
     * @param triggered  {@code true} if the event was triggered, {@code false} otherwise.
     */
    public void recordPreEvent(String eventName, boolean triggered) {
        database.addPreEventValue(eventName, triggered);
    }

    /**
     * Records whether a specific post-event was triggered.
     *
     * @param eventName  The name of the post-event.
     * @param triggered  {@code true} if the event was triggered, {@code false} otherwise.
     */
    public void recordPostEvent(String eventName, boolean triggered) {
        database.addPostEventValue(eventName, triggered);
    }

    /**
     * Retrieves all recorded values for a specific property.
     *
     * @param propertyName The name of the property.
     * @return A list of recorded values for the property.
     */
    public List<Object> getPropertyValues(String propertyName) {
        return database.getPropertyColumnAsList(propertyName);
    }

    /**
     * Retrieves all recorded trigger statuses for a specific pre-event.
     *
     * @param eventName The name of the pre-event.
     * @return A list of {@code Boolean} values indicating whether the event was triggered.
     */
    public List<Boolean> getPreEventTriggers(String eventName) {
        return (List<Boolean>) (List<?>) database.getPreEventColumnAsList(eventName);
    }

    /**
     * Retrieves all recorded trigger statuses for a specific post-event.
     *
     * @param eventName The name of the post-event.
     * @return A list of {@code Boolean} values indicating whether the event was triggered.
     */
    public List<Boolean> getPostEventTriggers(String eventName) {
        return (List<Boolean>) (List<?>) database.getPostEventColumnAsList(eventName);
    }

    /**
     * Disconnects the database used by this attribute set.
     * Ensures any open connections are closed properly.
     */
    public void disconnectDatabase() {
        database.disconnect();
    }
}
