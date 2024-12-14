package attributedatabases;

import java.util.List;

public abstract class AttributeResultsDatabase {
    private final String databasePath;

    public AttributeResultsDatabase(String databasePath) {
        this.databasePath = databasePath;
    }

    public AttributeResultsDatabase() {
        this.databasePath = null;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void connect() {
        return;
    }

    public void disconnect() {
        return;
    }

    public abstract void addPropertyValue(String propertyName, Object propertyValue);
    public abstract void addPreEventTrigger(String preEventName, boolean preEventTrigger);
    public abstract void addPostEventTrigger(String postEventName, boolean postEventTrigger);

    public abstract void replacePropertyColumn(String propertyName, List<?> propertyValues);
    public abstract void replacePreEventTrigger(String preEventName, List<Boolean> preEventTriggers);
    public abstract void replacePostEventTrigger(String postEventName, List<Boolean> postEventTriggers);

    public abstract List<?> getPropertyColumnAsList(String propertyName);
    public abstract List<Boolean> getPreEventColumnAsList(String preEventName);
    public abstract List<Boolean> getPostEventColumnAsList(String postEventName);
}
