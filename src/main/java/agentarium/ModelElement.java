package agentarium;

import agentarium.attributes.AttributeSetCollection;

public abstract class ModelElement {
    private final String name;
    private AttributeSetCollection attributeSetCollection;

    public ModelElement(String name, AttributeSetCollection attributeSetCollection) {
        this.name = name;
        this.attributeSetCollection = attributeSetCollection;
    }

    public String getName() {
        return name;
    }

    public AttributeSetCollection getAttributeSetCollection() {
        return attributeSetCollection;
    }

    public void setup() {
        attributeSetCollection.setup(getName());
    }

    public abstract void run();
}
