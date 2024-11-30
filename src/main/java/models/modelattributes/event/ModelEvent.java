package models.modelattributes.event;

public abstract class ModelEvent {
    private static int modelEventCount = 0;

    private final String name;
    private final boolean isRecorded;

    public ModelEvent(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        modelEventCount++;
    }

    public ModelEvent(boolean isRecorded) {
        this("Property Event " + modelEventCount, isRecorded);
    }

    public ModelEvent(String name) {
        this(name, true);
    }

    public ModelEvent() {
        this("Property Event " + modelEventCount, true);
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
