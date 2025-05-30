package agentarium.attributes;

/**
 * Represents a named attribute associated with an agent or environment.
 *
 * <p>Attributes define a unit of state or behaviour that is executed during a simulation tick
 * via the {@link #run()} method. Attributes may optionally be marked as "recorded" for output.
 */
public abstract class Attribute {

    /** Unique name identifying this attribute */
    private final String name;

    /** Whether this attribute should be recorded in results output */
    private final boolean isRecorded;

    /**
     * Constructs an attribute with the given name and recording flag.
     *
     * @param name the name of the attribute
     * @param isRecorded whether the attribute should be included in output
     */
    public Attribute(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
    }

    /**
     * @return the name of the attribute
     */
    public String getName() {
        return name;
    }

    /**
     * @return true if the attribute should be recorded in results
     */
    public boolean isRecorded() {
        return isRecorded;
    }

    /**
     * Runs the logic associated with this attribute.
     * Called once per simulation tick.
     */
    public abstract void run();
}
