package models.modelattributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAttributeResults {
    private final List<ModelAttributeSetResults> modelAttributeSetResultsList = new ArrayList<ModelAttributeSetResults>();
    private final Map<String, ModelAttributeSetResults> modelAttributeSetResultsMap = new HashMap<String, ModelAttributeSetResults>();

    public void setup(List<ModelAttributeSet> attributesSets) {
        for (ModelAttributeSet attributeSet : attributesSets) {
            ModelAttributeSetResults modelAttributeSetResults = new ModelAttributeSetResults(attributeSet);
            modelAttributeSetResultsList.add(modelAttributeSetResults);
            modelAttributeSetResultsMap.put(attributeSet.name(), modelAttributeSetResults);
        }
    }

    public ModelAttributeSetResults getAttributeResults(String attributeSetName) {
        return modelAttributeSetResultsMap.get(attributeSetName);
    }

    public ModelAttributeSetResults getAttributeResultsByIndex(int index) {
        return modelAttributeSetResultsList.get(index);
    }

    public int getAttributeSetCount() {
        return modelAttributeSetResultsList.size();
    }
}
