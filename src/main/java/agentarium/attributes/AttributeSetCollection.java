package agentarium.attributes;

import java.util.List;
import java.util.Map;

public class AttributeSetCollection {
    private Map<String, Integer> attributeSetIndexes;
    private List<AttributeSet> attributeSets;

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
        Integer index = attributeSetIndexes.get(name);
        return attributeSets.get(index);
    }

    public AttributeSet get(int index) {
        return attributeSets.get(index);
    }

    public int size() {
        return attributeSets.size();
    }

    public void run() {
        for (AttributeSet attributeSet : attributeSets)
            attributeSet.run();
    }
}
