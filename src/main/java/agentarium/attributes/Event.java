package agentarium.attributes;

public abstract class Event {
    private static int eventCount = 0;

    private final String name;
    private final boolean isRecorded;

    public Event(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        eventCount++;
    }

    public Event(boolean isRecorded) {
        this("Event " + eventCount++, isRecorded);
    }

    public Event(String name) {
        this(name, true);
    }

    public Event() {
        this("Event " + eventCount++, true);
    }

    public String getName() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public abstract boolean isTriggered();

    public abstract void run();
}
