package models;

import agents.Agent;
import agents.AgentResults;
import agents.attributes.AgentAttributeSet;
import utilities.Database;

import java.util.List;

public abstract class ModelResults {
    private final Database database;

    public ModelResults(String name) {
        database = new Database(name + "_model_results.db");
    }

    protected void setAttributes(List<AgentAttributeSet> attributes) {
        for (AgentAttributeSet attribute : attributes) {
            String propertiesTable = getPropertiesTableName(attribute.name());
            String preEventsTable = getPreEventsTableName(attribute.name());
            String postEventsTable = getPostEventsTableName(attribute.name());

            database.createTable(propertiesTable, "id INTEGER PRIMARY KEY, property_name TEXT, value TEXT, type TEXT");
            database.createTable(preEventsTable, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
            database.createTable(postEventsTable, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
        }
    }

    private String getPropertiesTableName(String attributeName) {
        return attributeName + "_properties";
    }

    private String getPreEventsTableName(String attributeName) {
        return attributeName + "_pre_events";
    }

    private String getPostEventsTableName(String attributeName) {
        return attributeName + "_post_events";
    }

    public void storeAgentResults(String agentName, AgentResults agentResults) {
        for (AgentAttributeSet attribute : agentResults.getAttributes()) {
            String propertiesTable = getPropertiesTableName(attribute.name());
            String preEventsTable = getPreEventsTableName(attribute.name());
            String postEventsTable = getPostEventsTableName(attribute.name());

            // Store properties
            for (String propertyName : attribute.getProperties().getPropertyNames()) {
                Object value = attribute.getProperties().getPropertyValue(propertyName);
                String type = value.getClass().getName();
                String serialisedValue = Database.serialiseValue(value);
                database.insertData(propertiesTable, "property_name, value, type", "?, ?, ?", propertyName, serialisedValue, type);
            }

            // Store pre-events
            for (String eventName : attribute.getPreEvents().getEventNames()) {
                boolean triggered = attribute.getPreEvents().isEventTriggered(eventName);
                database.insertData(preEventsTable, "event_name, triggered", "?, ?", eventName, triggered);
            }

            // Store post-events
            for (String eventName : attribute.getPostEvents().getEventNames()) {
                boolean triggered = attribute.getPostEvents().isEventTriggered(eventName);
                database.insertData(postEventsTable, "event_name, triggered", "?, ?", eventName, triggered);
            }
        }
        agentResults.disconnectDatabase();
    }

    public void disconnectDatabase() {
        database.disconnect();
    }

    public void run(List<Agent> agents) {
        // TODO: Complete run method for ModelResults
    }

    public abstract void mergeWith(ModelResults otherResults);

    public abstract ModelResults duplicate();
}
