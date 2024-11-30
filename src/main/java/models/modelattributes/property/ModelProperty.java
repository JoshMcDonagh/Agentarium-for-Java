package models.modelattributes.property;

public abstract class ModelProperty<T> {
    private static int modelPropertyCount = 0;

    private final String name;
    private final boolean isRecorded;

    public ModelProperty(String name, boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
        modelPropertyCount++;
    }

    public ModelProperty(boolean isRecorded) {
        this("Model Property " + modelPropertyCount, isRecorded);
    }

    public ModelProperty(String name) {
        this(name, true);
    }

    public ModelProperty() {
        this("Model Property " + modelPropertyCount, true);
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
