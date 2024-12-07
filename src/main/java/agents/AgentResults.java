package agents;

import agents.attributes.AgentAttributeSet;
import utilities.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentResults {
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
        }

        return database;
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
        String serialisedValue =  Database.serialiseValue(value);
        database.insertData(tableName, "property_name, value", "?, ?, ?", propertyName, serialisedValue, type);
    }

    public void recordPreEvent(AgentAttributeSet attribute, String eventName, boolean triggered) {
        String tableName = getPreEventsTableName(attribute.name());
        database.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public void recordPostEvent(AgentAttributeSet attribute, String eventName, boolean triggered) {
        String tableName = getPostEventsTableName(attribute.name());
        database.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public List<Double> getPropertyValues(AgentAttributeSet attribute, String propertyName) {
        List<Double> values = new ArrayList<>();
        String tableName = getPropertiesTableName(attribute.name());
        try {
            ResultSet results = database.queryData(
                    "SELECT value FROM " + tableName + " WHERE property_name = ?", propertyName
            );
            while (results.next()) {
                values.add(results.getDouble("value"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving property values for " + propertyName + ": " + e.getMessage());
        }
        return values;
    }

    public List<Boolean> getPreEventTriggers(AgentAttributeSet attribute, String eventName) {
        List<Boolean> triggers = new ArrayList<>();
        String tableName = getPreEventsTableName(attribute.name());
        try {
            ResultSet results = database.queryData(
                    "SELECT triggered FROM " + tableName + " WHERE event_name = ?", eventName
            );
            while (results.next()) {
                triggers.add(results.getBoolean("triggered"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pre-event triggers for " + eventName + ": " + e.getMessage());
        }
        return triggers;
    }

    public List<Boolean> getPostEventTriggers(AgentAttributeSet attribute, String eventName) {
        List<Boolean> triggers = new ArrayList<>();
        String tableName = getPostEventsTableName(attribute.name());
        try {
            ResultSet results = database.queryData(
                    "SELECT triggered FROM " + tableName + " WHERE event_name = ?", eventName
            );
            while (results.next()) {
                triggers.add(results.getBoolean("triggered"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving post-event triggers for " + eventName + ": " + e.getMessage());
        }
        return triggers;
    }

    public String getAgentName() {
        return agentName;
    }

    public List<AgentAttributeSet> getAttributes() {
        return attributes;
    }
}
