package agentarium.attributes.results.databases;

import java.util.List;

/**
 * Abstract base class representing a database for storing and retrieving simulation results
 * related to attribute sets, including properties and event values.
 *
 * <p>Concrete implementations may write to in-memory structures, files, or external systems.
 * This class supports both tick-by-tick updates and full-column writes.
 */
public abstract class AttributeSetResultsDatabase {

    /** Optional path or identifier for the backing database (e.g. file path) */
    private String databasePath = null;

    /**
     * Sets the path to the database used for output (e.g. CSV file, SQLite DB, etc.).
     *
     * @param databasePath a string identifying where the data will be stored
     */
    protected void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * @return the current path or identifier of the results database
     */
    public String getDatabasePath() {
        return databasePath;
    }

    /**
     * Opens the database or output stream for writing.
     *
     * <p>Default implementation does nothing. Subclasses should override if needed.
     */
    public void connect() {
        // Default implementation: No operation
        return;
    }

    /**
     * Closes the database or output stream.
     *
     * <p>Default implementation does nothing. Subclasses should override if needed.
     */
    public void disconnect() {
        // Default implementation: No operation
        return;
    }

    /**
     * Adds a new property value to the results database for the current tick.
     *
     * @param propertyName the name of the property
     * @param propertyValue the value to record
     * @param <T> the value type
     */
    public abstract <T> void addPropertyValue(String propertyName, T propertyValue);

    /**
     * Adds a new pre-event value (e.g. trigger status) for the current tick.
     *
     * @param preEventName the name of the pre-event
     * @param preEventValue the value to record
     * @param <T> the value type
     */
    public abstract <T> void addPreEventValue(String preEventName, T preEventValue);

    /**
     * Adds a new post-event value (e.g. trigger status) for the current tick.
     *
     * @param postEventName the name of the post-event
     * @param postEventValue the value to record
     * @param <T> the value type
     */
    public abstract <T> void addPostEventValue(String postEventName, T postEventValue);

    /**
     * Replaces or defines the entire set of values for a given property.
     *
     * @param propertyName the name of the property
     * @param propertyValues the full list of property values
     */
    public abstract void setPropertyColumn(String propertyName, List<Object> propertyValues);

    /**
     * Replaces or defines the full column of values for a pre-event.
     *
     * @param preEventName the name of the pre-event
     * @param preEventValues the full list of values
     */
    public abstract void setPreEventColumn(String preEventName, List<Object> preEventValues);

    /**
     * Replaces or defines the full column of values for a post-event.
     *
     * @param postEventName the name of the post-event
     * @param postEventValues the full list of values
     */
    public abstract void setPostEventColumn(String postEventName, List<Object> postEventValues);

    /**
     * Retrieves the full column of recorded values for a property.
     *
     * @param propertyName the name of the property
     * @return a list of recorded property values
     */
    public abstract List<Object> getPropertyColumnAsList(String propertyName);

    /**
     * Retrieves the full column of recorded values for a pre-event.
     *
     * @param preEventName the name of the pre-event
     * @return a list of recorded pre-event values
     */
    public abstract List<Object> getPreEventColumnAsList(String preEventName);

    /**
     * Retrieves the full column of recorded values for a post-event.
     *
     * @param postEventName the name of the post-event
     * @return a list of recorded post-event values
     */
    public abstract List<Object> getPostEventColumnAsList(String postEventName);
}
