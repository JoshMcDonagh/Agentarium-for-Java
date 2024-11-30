package agents.attributes.event;

public abstract class AgentEvent {
    private static int agentEventCount = 0;

    private final String name;
    private final boolean isRecorded;

    public AgentEvent(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        agentEventCount++;
    }

    public AgentEvent(boolean isRecorded) {
        this("Agent Event " + agentEventCount, isRecorded);
    }

    public AgentEvent(String name) {
        this(name, true);
    }

    public AgentEvent() {
        this("Agent Event " + agentEventCount, true);
    }

    public String name() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public abstract boolean isTriggered();
    public abstract void run();
}
