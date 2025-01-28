package agentarium.attributes;

import java.util.List;
import java.util.Map;

public abstract class Attributes {
    private Map<String, Integer> attributeIndexes;
    private List<Attribute> attributes;

    protected void addAttribute(Attribute attribute) {
        int index;

        if (attributeIndexes.containsKey(attribute.getName()))
            index = attributeIndexes.get(attribute.getName());
        else {
            index = attributes.size();
        }

        attributes.set(index, attribute);
    }

    protected Attribute getAttribute(String attributeName) {
        int index = attributeIndexes.get(attributeName);
        return attributes.get(index);
    }

    protected Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    public int size() {
        return attributes.size();
    }

    public abstract void run();
}
