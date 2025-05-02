package agentarium.attributes;

import agentarium.attributes.results.AttributeSetResults;

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

    public void run(AttributeSetResults attributeSetResults) {
        preEvents.run();
        recordPreEvents(attributeSetResults);

        properties.run();
        recordProperties(attributeSetResults);

        postEvents.run();
        recordPostEvents(attributeSetResults);
    }

    private void recordPreEvents(AttributeSetResults attributeSetResults) {
        for (int i = 0; i < preEvents.size(); i++) {
            Event event = preEvents.get(i);
            if (event.isRecorded())
                attributeSetResults.recordPreEvent(event.getName(), event.isTriggered());
        }
    }

    private void recordProperties(AttributeSetResults attributeSetResults) {
        for (int i = 0; i < properties.size(); i++) {
            Property<?> property = properties.get(i);
            if (property.isRecorded())
                attributeSetResults.recordProperty(property.getName(), property.get());
        }
    }

    private void recordPostEvents(AttributeSetResults attributeSetResults) {
        for (int i = 0; i < postEvents.size(); i++) {
            Event event = postEvents.get(i);
            if (event.isRecorded())
                attributeSetResults.recordPostEvent(event.getName(), event.isTriggered());
        }
    }
}
