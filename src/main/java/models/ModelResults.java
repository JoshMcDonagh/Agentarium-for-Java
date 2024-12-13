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
                    List<String> propertyNamesList = agentResults.getPropertyNamesList();
                    for (String propertyName : propertyNamesList) {
                        Class<?> propertyType = agentResults.getPropertyClass(propertyName);
                        List<?> modelValues = getPropertyValues(attributeName, propertyName, propertyType);
                        List<?> agentValues = agentResults.getPropertyValues(attributeName, propertyName, propertyType);
                        List<?> newModelValues = mergeAgentPropertyResultsWithModelResults(attributeName, propertyName, modelValues, agentValues);
                        database.replaceColumn(propertiesTableName, propertyName, newModelValues);
                    }
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

    protected abstract List<?> mergeAgentPropertyResultsWithModelResults(
            String attributeName,
            String propertyName,
            List<?> modelResultsAgentPropertyValues,
            List<?> agentResultsAgentPropertyValues
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
            List<?> agentResultsAgentPostEventTriggers
    );

    protected abstract <T> List<T> mergePropertyResults(
            String attributeName,
            String propertyName,
            List<T> firstResultsAgentPropertyValues,
            List<T> secondResultsAgentPropertyValues
    );

    protected abstract List<Boolean> mergePreEventResults(
            String attributeName,
            String preEventName,
            List<Boolean> firstResultsAgentPreEventTriggers,
            List<Boolean> secondResultsAgentPreEventTriggers
    );

    protected abstract List<Boolean> mergePostEventResults(
            String attributeName,
            String postEventName,
            List<Boolean> firstResultsAgentPostEventTriggers,
            List<Boolean> secondResultsAgentPostEventTriggers
    );
}
