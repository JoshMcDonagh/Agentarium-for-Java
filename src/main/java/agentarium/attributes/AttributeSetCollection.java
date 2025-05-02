package agentarium.attributes;

import agentarium.attributes.results.AttributeSetCollectionResults;
import com.google.gson.reflect.TypeToken;
import utils.DeepCopier;

import java.util.List;
import java.util.Map;

public class AttributeSetCollection {
    private String modelElementName;
    private Map<String, Integer> attributeSetIndexes;
    private List<AttributeSet> attributeSets;

    private AttributeSetCollectionResults attributeSetCollectionResults;

    public void add(AttributeSet attributeSet) {
        Integer index = attributeSets.size();
        attributeSetIndexes.put(attributeSet.getName(), index);
        attributeSets.add(attributeSet);
    }

    public void add(List<AttributeSet> attributeSets) {
        for (AttributeSet attributeSet : attributeSets)
            add(attributeSet);
    }

    public AttributeSet get(String name) {
        int index = attributeSetIndexes.get(name);
        return attributeSets.get(index);
    }

    public AttributeSet get(int index) {
        return attributeSets.get(index);
    }

    public int size() {
        return attributeSets.size();
    }

    public void setup(String modelElementName) {
        this.modelElementName = modelElementName;
        attributeSetCollectionResults = new AttributeSetCollectionResults();
        attributeSetCollectionResults.setup(modelElementName, attributeSets);
    }

    public String getModelElementName() {
        return modelElementName;
    }

    public AttributeSetCollectionResults getResults() {
        return attributeSetCollectionResults;
    }

    public void run() {
        for (AttributeSet attributeSet : attributeSets)
            attributeSet.run(getResults().getAttributeSetResults(attributeSet.getName()));
    }
    
    public AttributeSetCollection deepCopyDuplicate() {
        return DeepCopier.deepCopy(this, new TypeToken<AttributeSetCollection>() {}.getType());
    }
}
