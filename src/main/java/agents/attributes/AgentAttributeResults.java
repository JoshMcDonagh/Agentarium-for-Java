package agents.attributes;

import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;
import utilities.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentAttributeResults {
    private String agentName;
    private String attributeName;
    private AgentAttributeSet agentAttributeSet;
    private Database database;

    private List<String> propertyNamesList = new ArrayList<String>();
    private Map<String, Class<?>> propertyTypesMap = new HashMap<String, Class<?>>();
    private List<String> preEventNamesList = new ArrayList<String>();
    private List<String> postEventNamesList = new ArrayList<String>();

    public AgentAttributeResults(String agentName, AgentAttributeSet agentAttributeSet) {
        this.agentName = agentName;
        attributeName = agentAttributeSet.name();
        this.agentAttributeSet = agentAttributeSet;
        database = new Database(agentName);

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

    private Database createDatabase() {
        Database database = new Database(agentName + "_" + attributeName + "_agent_attribute_results.db");

        String propertiesTableName = getPropertiesTableName();
        String preEventsTableName = getPreEventsTableName();
        String postEventsTableName = getPostEventsTableName();

        database.createTable(propertiesTableName, "id INTEGER PRIMARY KEY, property_name TEXT, value TEXT, type TEXT");
        database.createTable(preEventsTableName, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
        database.createTable(postEventsTableName, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");

        return database;
    }

    private String getPropertiesTableName() {
        return agentName + "_" + attributeName + "_properties";
    }

    private String getPreEventsTableName() {
        return agentName + "_" + attributeName + "_pre_events";
    }

    private String getPostEventsTableName() {
        return agentName + "_" + attributeName + "_post_events";
    }

    public String getAgentName() {
        return agentName;
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
        String tableName = getPropertiesTableName();
        String type = value.getClass().getName();
        String serialisedValue = Database.serialiseValue(value);
        database.insertData(tableName, "property_name, value, type", "?, ?, ?", propertyName, serialisedValue, type);
    }

    public void recordPreEvent(String eventName, boolean triggered) {
        String tableName = getPreEventsTableName();
        database.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public void recordPostEvent(String eventName, boolean triggered) {
        String tableName = getPostEventsTableName();
        database.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public <T> List<T> getPropertyValues(String propertyName, Class<T> type) {
        return database.getColumnAsList(attributeName, propertyName, type);
    }

    public List<Boolean> getPreEventTriggers(String eventName) {
        return getEventTriggers(getPreEventsTableName(), eventName);
    }

    public List<Boolean> getPostEventTriggers(String eventName) {
        return getEventTriggers(getPostEventsTableName(), eventName);
    }

    private List<Boolean> getEventTriggers(String tableName, String eventName) {
        return database.getColumnAsList(tableName, eventName, Boolean.class);
    }

    public void disconnectDatabase() {
        database.disconnect();
    }
}
