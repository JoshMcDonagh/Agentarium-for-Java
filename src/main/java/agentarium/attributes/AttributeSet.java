package agentarium.attributes;

public class AttributeSet {
    private static int attributeSetCount = 0;

    private final String name;

    public AttributeSet(String name) {
        this.name = name;
        attributeSetCount++;
    }

    public AttributeSet() {
        this("Attribute set " + attributeSetCount);
    }

    public String getName() {
        return name;
    }
}
