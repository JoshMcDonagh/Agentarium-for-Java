package agentarium.attributes;

public abstract class Property<T> {
    private static int propertyCount = 0;

    private final String name;
    private final boolean isRecorded;
    private final Class<T> type;

    public Property(String name, boolean isRecorded, Class<T> type) {
        this.name = name;
        this.isRecorded = isRecorded;
        this.type = type;
        propertyCount++;
    }

    public Property(boolean isRecorded, Class<T> type) {
        this("Property " + propertyCount, isRecorded, type);
    }

    public Property(String name, Class<T> type) {
        this(name, true, type);
    }

    public Property(Class<T> type) {
        this("Property " + propertyCount, true, type);
    }

    public String getName() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public Class<T> getType() {
        return type;
    }

    public abstract void set(T value);
    public abstract T get();
    public abstract void run();
}
