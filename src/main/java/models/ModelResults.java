package models;

import agents.Agent;
import agents.AgentResults;
import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;
import utilities.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Double> getPropertyValues(String attributeName, String propertyName) {
        List<Double> values = new ArrayList<>();
        String tableName = getPropertiesTableName(attributeName);
        try {
            ResultSet databaseResults = database.queryData(
                    "SELECT value FROM " + tableName + " WHERE property_name = ?", propertyName
            );
            while (databaseResults.next())
                values.add(databaseResults.getDouble("value"));
        } catch (SQLException e) {
            System.err.println("Error retrieving property values for " + propertyName + ": " + e.getMessage());
        }
        return values;
    }

    public List<Boolean> getPreEventTriggers(String attributeName, String eventName) {
        List<Boolean> triggers = new ArrayList<>();
        String tableName = getPreEventsTableName(attributeName);
        try {
            ResultSet databaseResults = database.queryData(
                    "SELECT triggered FROM " + tableName + " WHERE event_name = ?", eventName
            );
            while (databaseResults.next())
                triggers.add(databaseResults.getBoolean("triggered"));
        } catch (SQLException e) {
            System.err.println("Error retrieving pre-event triggers for " + eventName + ": " + e.getMessage());
        }
        return triggers;
    }

    public List<Boolean> getPostEventTriggers(String attributeName, String eventName) {
        List<Boolean> triggers = new ArrayList<>();
        String tableName = getPostEventsTableName(attributeName);
        try {
            ResultSet databaseResults = database.queryData(
                    "SELECT triggered FROM " + tableName + " WHERE event_name = ?", eventName
            );
            while (databaseResults.next())
                triggers.add(databaseResults.getBoolean("triggered"));
        } catch (SQLException e) {
            System.err.println("Error retrieving post-event triggers for " + eventName + ": " + e.getMessage());
        }
        return triggers;
    }

    public void disconnectDatabase() {
        database.disconnect();
    }

    public void run(List<Agent> agents) {
        for (Agent agent : agents) {
            List<AgentAttributeSet> agentAttributeSetList = agent.getAttributeSetList();
            AgentResults agentResults = agent.getResults();
            for (AgentAttributeSet agentAttributeSet : agentAttributeSetList) {
                String attributeName = agentAttributeSet.name();
                List<AgentProperty<?>> agentPropertiesList = agentAttributeSet.getProperties().getPropertiesList();
                String propertiesTableName = getPropertiesTableName(attributeName);
                for (AgentProperty<?> agentProperty : agentPropertiesList) {
                    // TODO: Implement agent property merging
                }

                List<AgentEvent> agentPreEventsList = agentAttributeSet.getPreEvents().getEventsList();
                String preEventsTableName = getPreEventsTableName(attributeName);
                for (AgentEvent agentEvent : agentPreEventsList) {
                    // TODO: Implement agent pre-event merging
                }

                List<AgentEvent> agentPostEventsList = agentAttributeSet.getPostEvents().getEventsList();
                String postEventsTableName = getPostEventsTableName(attributeName);
                for (AgentEvent agentEvent : agentPostEventsList) {
                    // TODO: Implement agent post-event merging
                }
            }
        }
    }

    public abstract void mergeWith(ModelResults otherResults);

    public abstract ModelResults duplicate();

    protected abstract <T> List<T> mergeAgentPropertyResultsWithModelResults(
            String attributeName,
            String propertyName,
            List<T> modelResultsAgentPropertyValues,
            List<T> agentResultsAgentPropertyValues
    );

    protected abstract List<Boolean> mergeAgentPreEventResultsWithModelResults(
            String attributeName,
            String preEventName,
            List<Boolean> modelResultsAgentPreEventTriggers,
            List<Boolean> agentResultsAgentPreEventTriggers
    );

    protected abstract List<Boolean> mergeAgentPostEventResultsWithModelResults(
            String attributeName,
            String postEventName,
            List<Boolean> modelResultsAgentPostEventTriggers,
            List<Boolean> agentResultsAgentPostEventTriggers
    );

    protected abstract <T> List<T> mergePropertyResults(
            String attributeName,
            String propertyName,
            List<T> modelResultsAgentPropertyValues,
            List<T> agentResultsAgentPropertyValues
    );

    protected abstract List<Boolean> mergePreEventResults(
            String attributeName,
            String preEventName,
            List<Boolean> modelResultsAgentPreEventTriggers,
            List<Boolean> agentResultsAgentPreEventTriggers
    );

    protected abstract List<Boolean> mergePostEventResults(
            String attributeName,
            String postEventName,
            List<Boolean> modelResultsAgentPostEventTriggers,
            List<Boolean> agentResultsAgentPostEventTriggers
    );
}
