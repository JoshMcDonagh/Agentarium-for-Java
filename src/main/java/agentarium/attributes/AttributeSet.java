package agentarium.attributes;

import agentarium.attributes.results.AttributeSetResults;

/**
 * Represents a named set of attributes grouped into pre-events, properties, and post-events.
 *
 * <p>This class defines the execution and data-recording logic for all attributes associated
 * with an agent or environment component. It is intended to be run once per simulation tick,
 * coordinating execution order and conditional recording of attribute values.
 */
public class AttributeSet {

    /** Global counter used to generate default names for unnamed attribute sets */
    private static int attributeSetCount = 0;

    /** The name of this attribute set */
    private final String name;

    /** Events to run before the properties — typically trigger inputs or conditions */
    private final Events preEvents;

    /** Stateful attributes that hold and expose typed values */
    private final Properties properties;

    /** Events to run after properties — typically responses or outcomes */
    private final Events postEvents;

    /**
     * Constructs a fully specified attribute set.
     *
     * @param name the name of the attribute set
     * @param preEvents the events to run before properties
     * @param properties the properties in this attribute set
     * @param postEvents the events to run after properties
     */
    public AttributeSet(String name, Events preEvents, Properties properties, Events postEvents) {
        this.name = name;
        this.preEvents = preEvents;
        this.properties = properties;
        this.postEvents = postEvents;
        attributeSetCount++;
    }

    /**
     * Constructs an attribute set with the given name and empty components.
     *
     * @param name the name of the attribute set
     */
    public AttributeSet(String name) {
        this(name, new Events(), new Properties(), new Events());
    }

    /**
     * Constructs an attribute set with a generated name and empty components.
     */
    public AttributeSet() {
        this("Attribute set " + attributeSetCount);
    }

    /** @return the name of this attribute set */
    public String getName() {
        return name;
    }

    /** @return the collection of pre-event attributes */
    public Events getPreEvents() {
        return preEvents;
    }

    /** @return the collection of property attributes */
    public Properties getProperties() {
        return properties;
    }

    /** @return the collection of post-event attributes */
    public Events getPostEvents() {
        return postEvents;
    }

    /**
     * Executes all events and properties in the defined order:
     * <ol>
     *     <li>Pre-events (conditionally triggered)</li>
     *     <li>Properties (always run)</li>
     *     <li>Post-events (conditionally triggered)</li>
     * </ol>
     *
     * <p>If attributes are marked as recorded, their values or trigger states are stored in the
     * provided {@link AttributeSetResults} container.
     *
     * @param attributeSetResults the results object used to record values for this tick
     */
    public void run(AttributeSetResults attributeSetResults) {
        preEvents.run();
        recordPreEvents(attributeSetResults);

        properties.run();
        recordProperties(attributeSetResults);

        postEvents.run();
        recordPostEvents(attributeSetResults);
    }

    /** Records all pre-event trigger states if they are marked for recording */
    private void recordPreEvents(AttributeSetResults attributeSetResults) {
        for (int i = 0; i < preEvents.size(); i++) {
            Event event = preEvents.get(i);
            if (event.isRecorded())
                attributeSetResults.recordPreEvent(event.getName(), event.isTriggered());
        }
    }

    /** Records all property values if they are marked for recording */
    private void recordProperties(AttributeSetResults attributeSetResults) {
        for (int i = 0; i < properties.size(); i++) {
            Property<?> property = properties.get(i);
            if (property.isRecorded())
                attributeSetResults.recordProperty(property.getName(), property.get());
        }
    }

    /** Records all post-event trigger states if they are marked for recording */
    private void recordPostEvents(AttributeSetResults attributeSetResults) {
        for (int i = 0; i < postEvents.size(); i++) {
            Event event = postEvents.get(i);
            if (event.isRecorded())
                attributeSetResults.recordPostEvent(event.getName(), event.isTriggered());
        }
    }
}
