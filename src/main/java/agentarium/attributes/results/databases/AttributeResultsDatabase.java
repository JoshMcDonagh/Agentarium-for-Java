package agentarium.attributes.results.databases;

import java.util.List;

public abstract class AttributeResultsDatabase {
    private String databasePath = null;

    protected void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void connect() {
        // Default implementation: No-op
        return;
    }

    public void disconnect() {
        // Default implementation: No-op
        return;
    }

    public abstract <T> void addPropertyValue(String propertyName, T propertyValue);

    public abstract <T> void addPreEventValue(String preEventName, T preEventValue);

    public abstract <T> void addPostEventValue(String postEventName, T postEventValue);

    public abstract void setPropertyColumn(String propertyName, List<Object> propertyValues);

    public abstract void setPreEventColumn(String preEventName, List<Object> preEventValues);

    public abstract void setPostEventColumn(String postEventName, List<Object> postEventValues);

    public abstract List<Object> getPropertyColumnAsList(String propertyName);

    public abstract List<Object> getPreEventColumnAsList(String preEventName);

    public abstract List<Object> getPostEventColumnAsList(String postEventName);
}
