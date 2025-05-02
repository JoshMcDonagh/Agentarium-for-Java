package agentarium.attributes.results;

import agentarium.attributes.AttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeSetCollectionResults {
    private String modelElementName;

    private final List<AttributeSetResults> attributeSetResultsList = new ArrayList<>();

    private final Map<String, AttributeSetResults> attributeSetResultsMap = new HashMap<>();

    public void setup(String modelElementName, List<AttributeSet> attributeSets) {
        this.modelElementName = modelElementName;

        for (AttributeSet attributeSet : attributeSets) {
            AttributeSetResults attributeSetResults = new AttributeSetResults(this.modelElementName, attributeSet);
            attributeSetResultsList.add(attributeSetResults);
            attributeSetResultsMap.put(attributeSet.getName(), attributeSetResults);
        }
    }

    public AttributeSetResults getAttributeSetResults(String attributeSetName) {
        return attributeSetResultsMap.get(attributeSetName);
    }

    public AttributeSetResults getAttributeSetResults(int index) {
        return attributeSetResultsList.get(index);
    }

    public int getAttributeSetCount() {
        return attributeSetResultsList.size();
    }

    public String getModelElementName() {
        return modelElementName;
    }

    public void disconnectDatabases() {
        for (AttributeSetResults attributeSetResults : attributeSetResultsList)
            attributeSetResults.disconnectDatabase();

        attributeSetResultsMap.clear();
        attributeSetResultsList.clear();
    }
}
