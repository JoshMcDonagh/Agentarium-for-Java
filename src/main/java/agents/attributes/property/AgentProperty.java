package agents.attributes.property;

public abstract class AgentProperty<T> {
    private static int agentPropertyCount = 0;

    private final String name;
    private final boolean isRecorded;

    public AgentProperty(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        agentPropertyCount++;
    }

    public AgentProperty(boolean isRecorded) {
        this("Agent Property " + agentPropertyCount, isRecorded);
    }

    public AgentProperty(String name) {
        this(name, true);
    }

    public AgentProperty() {
        this("Agent Property " + agentPropertyCount, true);
    }

    public String name() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public Class<T> type() {
        return (Class<T>) getClass();
    }

    public abstract void set(T value);
    public abstract T get();
    public abstract void run();
}
