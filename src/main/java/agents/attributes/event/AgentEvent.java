package agents.attributes.event;

public abstract class AgentEvent {
    static int agentEventCount = 0;

    String name;
    boolean isRecorded;

    public AgentEvent(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        agentEventCount++;
    }

    public AgentEvent(boolean isRecorded) {
        this.name = "Agent Event " + agentEventCount;
        this.isRecorded = isRecorded;
        agentEventCount++;
    }

    public AgentEvent(String name) {
        this.name = name;
        this.isRecorded = true;
        agentEventCount++;
    }

    public AgentEvent() {
        this.name = "Agent Event " + agentEventCount;
        this.isRecorded = true;
        agentEventCount++;
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
