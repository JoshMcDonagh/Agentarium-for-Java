package agentarium.results;

import agentarium.ModelElement;
import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.results.AttributeSetCollectionResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelElementResults {
    private final Map<String, AttributeSetCollectionResults> attributeSetCollectionResultsMap = new HashMap<>();

    private final List<AttributeSetCollectionResults> attributeSetCollectionResultsList = new ArrayList<>();

    public ModelElementResults(List<? extends ModelElement> modelElements) {
        for (ModelElement modelElement : modelElements)
            addAttributeSetCollectionResults(modelElement.getAttributeSetCollection().getResults());
    }

    public ModelElementResults(ModelElement modelElement) {
        addAttributeSetCollectionResults(modelElement.getAttributeSetCollection().getResults());
    }

    private void addAttributeSetCollectionResults(AttributeSetCollectionResults results) {
        attributeSetCollectionResultsMap.put(results.getModelElementName(), results);
        attributeSetCollectionResultsList.add(results);
    }

    public void mergeWith(ModelElementResults otherResults) {
        attributeSetCollectionResultsMap.putAll(otherResults.attributeSetCollectionResultsMap);
        attributeSetCollectionResultsList.addAll(otherResults.attributeSetCollectionResultsList);
    }

    public List<Object> getPropertyValues(String modelElementName, String attributeSetName, String propertyName) {
        return attributeSetCollectionResultsMap.get(modelElementName)
                .getAttributeSetResults(attributeSetName)
                .getPropertyValues(propertyName);
    }

    public List<Boolean> getPreEventValues(String modelElementName, String attributeSetName, String eventName) {
        return attributeSetCollectionResultsMap.get(modelElementName)
                .getAttributeSetResults(attributeSetName)
                .getPreEventValues(eventName);
    }

    public List<Boolean> getPostEventValues(String modelElementName, String attributeSetName, String eventName) {
        return attributeSetCollectionResultsMap.get(modelElementName)
                .getAttributeSetResults(attributeSetName)
                .getPostEventValues(eventName);
    }

    public AttributeSetCollectionResults getAttributeSetCollectionResults(String modelElementName) {
        return attributeSetCollectionResultsMap.get(modelElementName);
    }

    public AttributeSetCollectionResults getAttributeSetCollectionResults(int index) {
        return attributeSetCollectionResultsList.get(index);
    }

    public int getAttributeSetCollectionCount() {
        return attributeSetCollectionResultsList.size();
    }

    public void disconnectDatabases() {
        for (AttributeSetCollectionResults attributeSetCollectionResults : attributeSetCollectionResultsList)
            attributeSetCollectionResults.disconnectDatabases();

        attributeSetCollectionResultsList.clear();
        attributeSetCollectionResultsMap.clear();
    }
}
