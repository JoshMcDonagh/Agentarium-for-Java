package agentarium.attributes;

import agentarium.ModelElement;
import agentarium.attributes.results.AttributeSetResults;
import utils.DeepCopyable;

/**
 * Represents a named collection of attributes, organised into pre-events, properties, and post-events.
 *
 * <p>This class defines the execution order and data recording logic for all attributes linked
 * to a model element such as an agent or environment. It is intended to be executed once per
 * simulation tick, coordinating the flow of behaviour and conditionally recording attribute states.
 */
public class AttributeSet implements DeepCopyable<AttributeSet> {

    /** Global counter used to generate default names for unnamed attribute sets */
    private static int attributeSetCount = 0;

    /** The name of this attribute set */
    private final String name;

    /** Events executed before properties — typically input checks or triggers */
    private final Events preEvents;

    /** Properties representing the stateful values of the model element */
    private final Properties properties;

    /** Events executed after properties — typically responses or side effects */
    private final Events postEvents;

    /**
     * Constructs an attribute set with a given name and specified components.
     *
     * @param name        the name of the attribute set
     * @param preEvents   the events to execute before properties
     * @param properties  the properties in this attribute set
     * @param postEvents  the events to execute after properties
     */
    public AttributeSet(String name, Events preEvents, Properties properties, Events postEvents) {
        this.name = name;
        this.preEvents = preEvents;
        this.properties = properties;
        this.postEvents = postEvents;
        attributeSetCount++;
    }

    /**
     * Constructs an attribute set with a given name and empty event/property collections.
     *
     * @param name the name of the attribute set
     */
    public AttributeSet(String name) {
        this(name, new Events(), new Properties(), new Events());
    }

    /**
     * Constructs an attribute set with a generated default name and empty components.
     */
    public AttributeSet() {
        this("Attribute set " + attributeSetCount);
    }

    /**
     * Associates all contained attributes with the specified model element.
     * Typically called during initialisation.
     *
     * @param associatedModelElement the model element to associate with
     */
    public void setAssociatedModelElement(ModelElement associatedModelElement) {
        preEvents.setAssociatedModelElement(associatedModelElement);
        properties.setAssociatedModelElement(associatedModelElement);
        postEvents.setAssociatedModelElement(associatedModelElement);
    }

    /** @return the name of this attribute set */
    public String getName() {
        return name;
    }

    /** @return the pre-event attributes of this set */
    public Events getPreEvents() {
        return preEvents;
    }

    /** @return the property attributes of this set */
    public Properties getProperties() {
        return properties;
    }

    /** @return the post-event attributes of this set */
    public Events getPostEvents() {
        return postEvents;
    }

    /**
     * Executes all attributes in the prescribed order:
     * <ol>
     *     <li>Pre-events (conditionally triggered)</li>
     *     <li>Properties (always executed)</li>
     *     <li>Post-events (conditionally triggered)</li>
     * </ol>
     *
     * <p>If any attribute is marked for recording, its value or triggered state is stored in the
     * provided {@link AttributeSetResults} container for this tick.
     *
     * @param attributeSetResults the results object used to collect recorded values
     */
    public void run(AttributeSetResults attributeSetResults) {
        preEvents.run();
        recordPreEvents(attributeSetResults);

        properties.run();
        recordProperties(attributeSetResults);

        postEvents.run();
        recordPostEvents(attributeSetResults);
    }

    /** Records the triggered state of pre-events if marked as recorded */
    private void recordPreEvents(AttributeSetResults attributeSetResults) {
        if (preEvents.getAssociatedModelElement().getModelElementAccessor().getModelClock().isWarmingUp())
            return;

        for (int i = 0; i < preEvents.size(); i++) {
            Event event = preEvents.get(i);
            if (event.isRecorded())
                attributeSetResults.recordPreEvent(event.getName(), event.isTriggered());
        }
    }

    /** Records the values of properties if marked as recorded */
    private void recordProperties(AttributeSetResults attributeSetResults) {
        if (properties.getAssociatedModelElement().getModelElementAccessor().getModelClock().isWarmingUp())
            return;

        for (int i = 0; i < properties.size(); i++) {
            Property<?> property = properties.get(i);
            if (property.isRecorded())
                attributeSetResults.recordProperty(property.getName(), property.get());
        }
    }

    /** Records the triggered state of post-events if marked as recorded */
    private void recordPostEvents(AttributeSetResults attributeSetResults) {
        if (postEvents.getAssociatedModelElement().getModelElementAccessor().getModelClock().isWarmingUp())
            return;

        for (int i = 0; i < postEvents.size(); i++) {
            Event event = postEvents.get(i);
            if (event.isRecorded())
                attributeSetResults.recordPostEvent(event.getName(), event.isTriggered());
        }
    }

    @Override
    public AttributeSet deepCopy() {
        return new AttributeSet(name, preEvents.deepCopy(), properties.deepCopy(), postEvents.deepCopy());
    }
}
