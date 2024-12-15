package agents.attributes;

import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;
import attributedatabases.AttributeResultsDatabase;
import attributedatabases.AttributeResultsDatabaseFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentAttributeSetResults {
    private final String agentName;
    private final String attributeName;
    private final AttributeResultsDatabase database;

    private final List<String> propertyNamesList = new ArrayList<String>();
    private final Map<String, Class<?>> propertyTypesMap = new HashMap<String, Class<?>>();
    private final List<String> preEventNamesList = new ArrayList<String>();
    private final List<String> postEventNamesList = new ArrayList<String>();

    public AgentAttributeSetResults(String agentName, AgentAttributeSet agentAttributeSet) {
        this.agentName = agentName;
        attributeName = agentAttributeSet.name();
        database = AttributeResultsDatabaseFactory.createDatabase();
        database.connect();

        List<AgentProperty<?>> propertiesList = agentAttributeSet.getProperties().getPropertiesList();
        for (AgentProperty<?> property : propertiesList) {
            if (property.isRecorded()) {
                propertyNamesList.add(property.name());
                propertyTypesMap.put(property.name(), property.type());
            }
        }

        List<AgentEvent> preEventList = agentAttributeSet.getPreEvents().getEventsList();
        for (AgentEvent event : preEventList) {
            if (event.isRecorded())
                preEventNamesList.add(event.name());
        }

        List<AgentEvent> postEventList = agentAttributeSet.getPostEvents().getEventsList();
        for (AgentEvent event : postEventList) {
            if (event.isRecorded())
                postEventNamesList.add(event.name());
        }
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public List<String> getPropertyNamesList() {
        return propertyNamesList;
    }

    public List<String> getPreEventNamesList() {
        return preEventNamesList;
    }

    public List<String> getPostEventNamesList() {
        return postEventNamesList;
    }

    public Class<?> getPropertyClass (String propertyName) {
        return propertyTypesMap.get(propertyName);
    }

    public void recordProperty(String propertyName, Object value) {
        database.addPropertyValue(propertyName, value);
    }

    public void recordPreEvent(String eventName, boolean triggered) {
        database.addPreEventTrigger(eventName, triggered);
    }

    public void recordPostEvent(String eventName, boolean triggered) {
        database.addPostEventTrigger(eventName, triggered);
    }

    public <T> List<T> getPropertyValues(String propertyName) {
        return database.getPropertyColumnAsList(propertyName);
    }

    public List<Boolean> getPreEventTriggers(String eventName) {
        return database.getPreEventColumnAsList(eventName);
    }

    public List<Boolean> getPostEventTriggers(String eventName) {
        return database.getPostEventColumnAsList(eventName);
    }

    public void disconnectDatabase() {
        database.disconnect();
    }
}
