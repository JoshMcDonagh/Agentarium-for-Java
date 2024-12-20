package agents.attributes.property;

/**
 * Represents a generic property associated with an agent.
 * This abstract class provides a foundation for creating specific types of agent properties with values of a specified type.
 *
 * @param <T> The type of the value associated with the property.
 */
public abstract class AgentProperty<T> {

    // Static counter to track the number of AgentProperty instances created.
    private static int agentPropertyCount = 0;

    // The name of the property.
    private final String name;

    // Flag indicating whether the property is recorded (used for logging or persistence purposes).
    private final boolean isRecorded;

    private final Class<T> type;

    /**
     * Constructs an AgentProperty with a specified name and recorded status.
     *
     * @param name       The name of the property.
     * @param isRecorded Whether the property is recorded.
     * @param type       Type of data stored.
     */
    public AgentProperty(String name, boolean isRecorded, Class<T> type) {
        this.name = name;
        this.isRecorded = isRecorded;
        this.type = type;
        agentPropertyCount++; // Increment the counter to track created properties.
    }

    /**
     * Constructs an AgentProperty with an auto-generated name and specified recorded status.
     *
     * @param isRecorded Whether the property is recorded.
     * @param type       Type of data stored.
     */
    public AgentProperty(boolean isRecorded, Class<T> type) {
        this("Agent Property " + agentPropertyCount, isRecorded, type);
    }

    /**
     * Constructs an AgentProperty with a specified name and assumes the property is recorded by default.
     *
     * @param name The name of the property.
     * @param type Type of data stored.
     */
    public AgentProperty(String name, Class<T> type) {
        this(name, true, type);
    }

    /**
     * Constructs an AgentProperty with an auto-generated name and assumes the property is recorded by default.
     *
     * @param type Type of data stored.
     */
    public AgentProperty(Class<T> type) {
        this("Agent Property " + agentPropertyCount, true, type);
    }

    /**
     * Retrieves the name of the property.
     *
     * @return The name of the property.
     */
    public String name() {
        return name;
    }

    /**
     * Indicates whether the property is recorded.
     *
     * @return {@code true} if the property is recorded; {@code false} otherwise.
     */
    public boolean isRecorded() {
        return isRecorded;
    }

    /**
     * Retrieves the class type of the property value.
     *
     * @return The class type of the value.
     */
    public Class<T> type() {
        return type;
    }

    /**
     * Sets the value of the property.
     *
     * @param value The value to set.
     */
    public abstract void set(T value);

    /**
     * Retrieves the value of the property.
     *
     * @return The value of the property.
     */
    public abstract T get();

    /**
     * Executes the behaviour associated with this property.
     * Subclasses should implement this method to define property-specific operations or updates.
     */
    public abstract void run();
}
