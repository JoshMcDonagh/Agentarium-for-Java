package agents.attributes;

import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;

/**
 * Represents a set of attributes for an agent, including properties and events.
 * Each attribute set contains properties, pre-events, and post-events that can be executed in sequence.
 */
public class AgentAttributeSet {

    // Static counter to assign unique default names to attribute sets.
    private static int agentAttributeCount = 0;

    // Container for properties associated with this attribute set.
    private final AgentProperties properties;

    // Container for pre-events associated with this attribute set.
    private final AgentEvents preEvents;

    // Container for post-events associated with this attribute set.
    private final AgentEvents postEvents;

    // Name of the attribute set.
    private final String name;

    /**
     * Constructs an `AgentAttributeSet` with specific components and a specific name.
     *
     * @param name       The name of the attribute set.
     * @param properties The `AgentProperties` instance.
     * @param preEvents  The `AgentEvents` instance for pre-events.
     * @param postEvents The `AgentEvents` instance for post-events.
     */
    public AgentAttributeSet(String name, AgentProperties properties, AgentEvents preEvents, AgentEvents postEvents) {
        this.name = name;
        this.properties = properties;
        this.preEvents = preEvents;
        this.postEvents = postEvents;
        agentAttributeCount++;
    }

    /**
     * Constructs an `AgentAttributeSet` with default components and a specific name.
     *
     * @param name The name of the attribute set.
     */
    public AgentAttributeSet(String name) {
        this(name, new AgentProperties(), new AgentEvents(), new AgentEvents());
    }

    /**
     * Constructs an `AgentAttributeSet` with default components and a default name.
     * The default name is generated based on the current value of the static counter.
     */
    public AgentAttributeSet() {
        this("Agent Attribute Set " + agentAttributeCount);
    }

    /**
     * Retrieves the name of the attribute set.
     *
     * @return The name of the attribute set.
     */
    public String name() {
        return name;
    }

    /**
     * Retrieves the collection of properties associated with this attribute set.
     *
     * @return An `AgentProperties` object containing the properties.
     */
    public AgentProperties getProperties() {
        return properties;
    }

    /**
     * Retrieves the collection of pre-events associated with this attribute set.
     *
     * @return An `AgentEvents` object containing the pre-events.
     */
    public AgentEvents getPreEvents() {
        return preEvents;
    }

    /**
     * Retrieves the collection of post-events associated with this attribute set.
     *
     * @return An `AgentEvents` object containing the post-events.
     */
    public AgentEvents getPostEvents() {
        return postEvents;
    }

    /**
     * Executes all pre-events, properties, and post-events in sequence.
     * The `run` method for each component is called to execute their respective logic.
     */
    public void run() {
        preEvents.run();
        properties.run();
        postEvents.run();
    }
}
