package agents;

import agents.attributes.AgentAttributes;
import utilities.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentResults {
    private String agentName;
    private List<AgentAttributes> attributes;

    public void setup(String agentName, List<AgentAttributes> attributes) {
        this.agentName = agentName;
        this.attributes = attributes;
        initialiseTables();
    }

    private void initialiseTables() {
        for (AgentAttributes attribute : attributes) {
            String propertiesTable = getPropertiesTableName(attribute.name());
            String preEventsTable = getPreEventsTableName(attribute.name());
            String postEventsTable = getPostEventsTableName(attribute.name());

            DatabaseHandler.createTable(propertiesTable, "id INTEGER PRIMARY KEY, property_name TEXT, value REAL");
            DatabaseHandler.createTable(preEventsTable, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
            DatabaseHandler.createTable(postEventsTable, "id INTEGER PRIMARY KEY, event_name TEXT, triggered BOOLEAN");
        }
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

    public void recordProperty(AgentAttributes attribute, String propertyName, double value) {
        String tableName = getPropertiesTableName(attribute.name());
        DatabaseHandler.insertData(tableName, "property_name, value", "?, ?", propertyName, value);
    }

    public void recordPreEvent(AgentAttributes attribute, String eventName, boolean triggered) {
        String tableName = getPreEventsTableName(attribute.name());
        DatabaseHandler.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public void recordPostEvent(AgentAttributes attribute, String eventName, boolean triggered) {
        String tableName = getPostEventsTableName(attribute.name());
        DatabaseHandler.insertData(tableName, "event_name, triggered", "?, ?", eventName, triggered);
    }

    public List<Double> getPropertyValues(AgentAttributes attribute, String propertyName) {
        List<Double> values = new ArrayList<>();
        String tableName = getPropertiesTableName(attribute.name());
        try {
            ResultSet results = DatabaseHandler.queryData(
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

    public List<Boolean> getPreEventTriggers(AgentAttributes attribute, String eventName) {
        List<Boolean> triggers = new ArrayList<>();
        String tableName = getPreEventsTableName(attribute.name());
        try {
            ResultSet results = DatabaseHandler.queryData(
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

    public List<Boolean> getPostEventTriggers(AgentAttributes attribute, String eventName) {
        List<Boolean> triggers = new ArrayList<>();
        String tableName = getPostEventsTableName(attribute.name());
        try {
            ResultSet results = DatabaseHandler.queryData(
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
}
