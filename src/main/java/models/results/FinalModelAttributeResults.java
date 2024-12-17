package models.results;

import models.modelattributes.results.ModelAttributeResults;

import java.util.List;

public class FinalModelAttributeResults {
    private final ModelAttributeResults modelAttributeResults;

    public FinalModelAttributeResults(ModelAttributeResults modelAttributeResults) {
        this.modelAttributeResults = modelAttributeResults;
    }

    public List<Object> getModelPropertyValues(String attributeName, String propertyName) {
        return modelAttributeResults.getAttributeResults(attributeName).getPropertyValues(propertyName);
    }

    public List<Boolean> getModelPreEventTriggers(String attributeName, String eventName) {
        return modelAttributeResults.getAttributeResults(attributeName).getPreEventTriggers(eventName);
    }

    public List<Boolean> getModelPostEventTriggers(String attributeName, String eventName) {
        return modelAttributeResults.getAttributeResults(attributeName).getPostEventTriggers(eventName);
    }

    public ModelAttributeResults get() {
        return modelAttributeResults;
    }

    public void disconnectDatabases() {
        modelAttributeResults.disconnectDatabases();
    }
}
