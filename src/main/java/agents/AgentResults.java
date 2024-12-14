package agents;

import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;
import utilities.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentResults {
    private List<String> propertyNamesList = new ArrayList<String>();
    private Map<String, Class<?>> propertyTypesMap = new HashMap<String, Class<?>>();
    private List<String> preEventNamesList = new ArrayList<String>();
    private List<String> postEventNamesList = new ArrayList<String>();

    private String agentName;
    private List<AgentAttributeSet> attributes;
    private Database database;

    public void setup(String agentName, List<AgentAttributeSet> attributes) {
        this.agentName = agentName;
        this.attributes = attributes;
        database = createDatabase();
    }

    private Database createDatabase() {
        Database database = new Database(agentName + "_agent_results.db");

        for (AgentAttributeSet attribute : attributes) {
            String propertiesTable = getPropertiesTableName(attribute.name());
            String preEventsTable = getPreEventsTableName(attribute.name());
            String postEventsTable = getPostEventsTableName(attribute.name());

            database.createTable(propertiesTable, "id INTEGER PRIMARY KEY, property_name TEXT, value TEXT, type TEXT");
            database.createTable(preEventsTable, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
            database.createTable(postEventsTable, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");

            List<AgentProperty<?>> propertiesList = attribute.getProperties().getPropertiesList();
            for (AgentProperty<?> property : propertiesList) {
                if (property.isRecorded()) {
                    propertyNamesList.add(property.name());
                    propertyTypesMap.put(property.name(), property.type());
                }
            }

            List<AgentEvent> preEventList = attribute.getPreEvents().getEventsList();
            for (AgentEvent event : preEventList) {
                if (event.isRecorded())
                    preEventNamesList.add(event.name());
            }

            List<AgentEvent> postEventList = attribute.getPostEvents().getEventsList();
            for (AgentEvent event : postEventList) {
                if (event.isRecorded())
                    postEventNamesList.add(event.name());
            }
        }

        return database;
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

    private String getPropertiesTableName(String attributeName) {
        return agentName + "_" + attributeName + "_properties";
    }

    private String getPreEventsTableName(String attributeName) {
        return agentName + "_" + attributeName + "_pre_events";
    }

    private String getPostEventsTableName(String attributeName) {
        return agentName + "_" + attributeName + "_post_events";
    }

    public void disconnectDatabase() {
        database.disconnect();
    }

    public void recordProperty(AgentAttributeSet attribute, String propertyName, Object value) {
        String tableName = getPropertiesTableName(attribute.name());
        String type = value.getClass().getName();
        String serializedValue = Database.serialiseValue(value);
        database.insertData(tableName, "property_name, value, type", "?, ?, ?", propertyName, serializedValue, type);
    }

    public void recordPreEvent(AgentAttributeSet attribute, String eventName, boolean triggered) {
        String tableName = getPreEventsTableName(attribute.name());
        database.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public void recordPostEvent(AgentAttributeSet attribute, String eventName, boolean triggered) {
        String tableName = getPostEventsTableName(attribute.name());
        database.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public <T> List<T> getPropertyValues(String attributeName, String propertyName, Class<T> type) {
        return database.getColumnAsList(attributeName, propertyName, type);
    }

    public List<Boolean> getPreEventTriggers(String attributeName, String eventName) {
        return getEventTriggers(getPreEventsTableName(attributeName), eventName);
    }

    public List<Boolean> getPostEventTriggers(String attributeName, String eventName) {
        return getEventTriggers(getPostEventsTableName(attributeName), eventName);
    }

    private List<Boolean> getEventTriggers(String tableName, String eventName) {
        return database.getColumnAsList(tableName, eventName, Boolean.class);
    }

    public String getAgentName() {
        return agentName;
    }

    public List<AgentAttributeSet> getAttributes() {
        return attributes;
    }
}
