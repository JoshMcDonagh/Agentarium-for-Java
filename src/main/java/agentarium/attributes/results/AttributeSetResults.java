package agentarium.attributes.results;

import agentarium.attributes.AttributeSet;
import agentarium.attributes.Event;
import agentarium.attributes.Property;
import agentarium.attributes.results.databases.AttributeResultsDatabase;
import agentarium.attributes.results.databases.AttributeResultsDatabaseFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeSetResults {
    private final String modelElementName;
    private final String attributeSetName;
    private final AttributeResultsDatabase database;

    private final List<String> propertyNamesList = new ArrayList<>();

    private final Map<String, Class<?>> propertyTypesMap = new HashMap<>();

    private final List<String> preEventNamesList = new ArrayList<>();

    private final List<String> postEventNamesList= new ArrayList<>();

    public AttributeSetResults(String modelElementName, AttributeSet attributeSet) {
        this.modelElementName = modelElementName;
        this.attributeSetName = attributeSet.getName();
        this.database = AttributeResultsDatabaseFactory.createDatabase();
        assert database != null;
        database.connect();

        for (int i = 0; i < attributeSet.getProperties().size(); i++) {
            Property<?> property = attributeSet.getProperties().get(i);
            if (property.isRecorded()) {
                propertyNamesList.add(property.getName());
                propertyTypesMap.put(property.getName(), property.getType());
            }
        }

        for (int i = 0; i < attributeSet.getPreEvents().size(); i++) {
            Event event = attributeSet.getPreEvents().get(i);
            if (event.isRecorded())
                preEventNamesList.add(event.getName());
        }

        for (int i = 0; i < attributeSet.getPostEvents().size(); i++) {
            Event event = attributeSet.getPostEvents().get(i);
            if (event.isRecorded())
                postEventNamesList.add(event.getName());
        }
    }

    public String getModelElementName() {
        return modelElementName;
    }

    public String getAttributeSetName() {
        return attributeSetName;
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

    public Class<?> getPropertyClass(String propertyName) {
        return propertyTypesMap.get(propertyName);
    }

    public void recordProperty(String propertyName, Object value) {
        database.addPropertyValue(propertyName, value);
    }

    public void recordPreEvent(String eventName, boolean isTriggered) {
        database.addPreEventValue(eventName, isTriggered);
    }

    public void recordPostEvent(String eventName, boolean isTriggered) {
        database.addPostEventValue(eventName, isTriggered);
    }

    public List<Object> getPropertyValues(String propertyName) {
        return database.getPropertyColumnAsList(propertyName);
    }

    public List<Boolean> getPreEventValues(String eventName) {
        return (List<Boolean>) (List<?>) database.getPreEventColumnAsList(eventName);
    }

    public List<Boolean> getPostEventValues(String eventName) {
        return (List<Boolean>) (List<?>) database.getPostEventColumnAsList(eventName);
    }

    public void disconnectDatabase() {
        database.disconnect();
    }
}
