package agentarium.attributes;

public class AttributeSet {
    private static int attributeSetCount = 0;

    private final String name;
    private final Events preEvents;
    private final Properties properties;
    private final Events postEvents;


    public AttributeSet(String name, Events preEvents, Properties properties, Events postEvents) {
        this.name = name;
        this.preEvents = preEvents;
        this.properties = properties;
        this.postEvents = postEvents;
        attributeSetCount++;
    }

    public AttributeSet(String name) {
        this(name, new Events(), new Properties(), new Events());
    }

    public AttributeSet() {
        this("Attribute set " + attributeSetCount);
    }

    public String getName() {
        return name;
    }

    public Events getPreEvents() {
        return preEvents;
    }

    public Properties getProperties() {
        return properties;
    }

    public Events getPostEvents() {
        return postEvents;
    }

    public void run() {
        preEvents.run();
        properties.run();
        postEvents.run();
    }
}
