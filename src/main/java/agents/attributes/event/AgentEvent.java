package agents.attributes.event;

/**
 * Abstract class representing an event associated with an agent.
 * Each event has a unique name, a flag indicating whether it is recorded,
 * and abstract methods to determine if the event is triggered and to define the event's behaviour.
 */
public abstract class AgentEvent {

    // Static counter to keep track of the number of AgentEvent instances created.
    private static int agentEventCount = 0;

    // Name of the event.
    private final String name;

    // Flag indicating whether the event is recorded.
    private final boolean isRecorded;

    /**
     * Constructs an AgentEvent with the specified name and recording status.
     *
     * @param name       the name of the event.
     * @param isRecorded whether the event is recorded.
     */
    public AgentEvent(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        agentEventCount++; // Increment the event counter each time an event is created.
    }

    /**
     * Constructs an AgentEvent with an auto-generated name based on the event count
     * and a specified recording status.
     *
     * @param isRecorded whether the event is recorded.
     */
    public AgentEvent(boolean isRecorded) {
        this("Agent Event " + agentEventCount, isRecorded);
    }

    /**
     * Constructs an AgentEvent with the specified name and a default recording status of {@code true}.
     *
     * @param name the name of the event.
     */
    public AgentEvent(String name) {
        this(name, true);
    }

    /**
     * Constructs an AgentEvent with an auto-generated name based on the event count
     * and a default recording status of {@code true}.
     */
    public AgentEvent() {
        this("Agent Event " + agentEventCount, true);
    }

    /**
     * Retrieves the name of the event.
     *
     * @return the name of the event.
     */
    public String name() {
        return name;
    }

    /**
     * Indicates whether the event is recorded.
     *
     * @return {@code true} if the event is recorded, {@code false} otherwise.
     */
    public boolean isRecorded() {
        return isRecorded;
    }

    /**
     * Determines whether the event is triggered.
     * Subclasses must provide the implementation for this method.
     *
     * @return {@code true} if the event is triggered, {@code false} otherwise.
     */
    public abstract boolean isTriggered();

    /**
     * Executes the behaviour associated with the event.
     * Subclasses must provide the implementation for this method.
     */
    public abstract void run();
}
