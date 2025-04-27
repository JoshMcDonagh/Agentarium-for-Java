package agentarium;

import agentarium.attributes.AttributeSetCollection;

public abstract class ModelElement {
    private final String name;
    private AttributeSetCollection attributeSets;

    public ModelElement(String name, AttributeSetCollection attributeSets) {
        this.name = name;
        this.attributeSets = attributeSets;
    }

    public String getName() {
        return name;
    }

    public AttributeSetCollection getAttributeSetCollection() {
        return attributeSets;
    }

    public abstract void run();
}
