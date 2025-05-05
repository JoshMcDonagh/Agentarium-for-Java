package agentarium;

import agentarium.attributes.AttributeSetCollection;

public abstract class ModelElement {
    private final String name;
    private AttributeSetCollection attributeSetCollection;
    private ModelElementAccessor modelElementAccessor;

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

    protected void setModelElementAccessor(ModelElementAccessor modelElementAccessor) {
        this.modelElementAccessor = modelElementAccessor;
    }

    public ModelElementAccessor getModelElementAccessor() {
        return modelElementAccessor;
    }

    public void setup() {
        attributeSetCollection.setup(getName());
    }

    public abstract void run();
}
