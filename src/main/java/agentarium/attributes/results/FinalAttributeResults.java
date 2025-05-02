package agentarium.attributes.results;

import agentarium.ModelElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalAttributeResults {
    private final Map<String, AttributeSetCollectionResults> attributeResultsMap = new HashMap<>();

    private final List<AttributeSetCollectionResults> attributeResultsList = new ArrayList<>();

    public FinalAttributeResults(List<ModelElement> modelElements) {
        for (ModelElement modelElement : modelElements) {

        }
    }
}
