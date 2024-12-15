package attributedatabases;

import java.util.List;

public abstract class AttributeResultsDatabase {
    private String databasePath;

    protected void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
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

    public abstract <T> void addPropertyValue(String propertyName, T propertyValue);
    public abstract <T> void addPreEventValue(String preEventName, T preEventValue);
    public abstract <T> void addPostEventValue(String postEventName, T postEventValue);

    public abstract void replacePropertyColumn(String propertyName, List<Object> propertyValues);
    public abstract void replacePreEventTrigger(String preEventName, List<Object> preEventValues);
    public abstract void replacePostEventTrigger(String postEventName, List<Object> postEventValues);

    public abstract List<Object> getPropertyColumnAsList(String propertyName);
    public abstract List<Object> getPreEventColumnAsList(String preEventName);
    public abstract List<Object> getPostEventColumnAsList(String postEventName);
}
