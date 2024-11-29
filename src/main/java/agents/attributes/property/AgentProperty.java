package agents.attributes.property;

public abstract class AgentProperty<T> {
    static int agentPropertyCount = 0;

    String name;
    boolean isRecorded;

    public AgentProperty(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        agentPropertyCount++;
    }

    public AgentProperty(boolean isRecorded) {
        this.name = "Agent Property " + agentPropertyCount;
        this.isRecorded = isRecorded;
        agentPropertyCount++;
    }

    public AgentProperty(String name) {
        this.name = name;
        this.isRecorded = true;
        agentPropertyCount++;
    }

    public AgentProperty() {
        this.name = "Agent Property " + agentPropertyCount;
        this.isRecorded = true;
        agentPropertyCount++;
    }

    public String name() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public abstract void set(T value);
    public abstract T get();
    public abstract void run();
}
