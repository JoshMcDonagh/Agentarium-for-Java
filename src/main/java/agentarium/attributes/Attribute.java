package agentarium.attributes;

public abstract class Attribute {
    private String name;
    private Boolean isRecorded;

    public Attribute(String name, Boolean isRecorded) {
        this.name = name;
        this.isRecorded = isRecorded;
    }

    public String getName() {
        return name;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public abstract void run();
}
