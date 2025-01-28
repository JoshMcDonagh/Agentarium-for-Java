package agentarium.attributes;

public abstract class Event extends Attribute {
    private static int eventCount = 0;

    public Event(String name, boolean isRecorded) {
        super(name, isRecorded);
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

    public abstract boolean isTriggered();

    public abstract void run();
}
