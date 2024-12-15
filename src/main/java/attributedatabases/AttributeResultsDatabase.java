package attributedatabases;

import java.util.List;

/**
 * Abstract base class for managing attribute results databases.
 * This class defines the structure and contract for specific implementations of
 * attribute databases, such as in-memory or disk-based storage.
 */
public abstract class AttributeResultsDatabase {
    // Path to the database (used in disk-based implementations)
    private String databasePath;

    /**
     * Sets the database path.
     *
     * @param databasePath Path to the database.
     */
    protected void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * Retrieves the database path.
     *
     * @return The path to the database.
     */
    public String getDatabasePath() {
        return databasePath;
    }

    /**
     * Connects to the database.
     * Default implementation does nothing. Specific implementations (e.g., disk-based) must override this.
     */
    public void connect() {
        // Default implementation: No-op
        return;
    }

    /**
     * Disconnects from the database.
     * Default implementation does nothing. Specific implementations (e.g., disk-based) must override this.
     */
    public void disconnect() {
        // Default implementation: No-op
        return;
    }

    /**
     * Adds a property value to the database.
     *
     * @param propertyName  Name of the property.
     * @param propertyValue Value of the property.
     * @param <T>           Type of the property value.
     */
    public abstract <T> void addPropertyValue(String propertyName, T propertyValue);

    /**
     * Adds a pre-event value to the database.
     *
     * @param preEventName  Name of the pre-event.
     * @param preEventValue Value of the pre-event.
     * @param <T>           Type of the pre-event value.
     */
    public abstract <T> void addPreEventValue(String preEventName, T preEventValue);

    /**
     * Adds a post-event value to the database.
     *
     * @param postEventName  Name of the post-event.
     * @param postEventValue Value of the post-event.
     * @param <T>            Type of the post-event value.
     */
    public abstract <T> void addPostEventValue(String postEventName, T postEventValue);

    /**
     * Replaces all values for a specific property column.
     *
     * @param propertyName  Name of the property.
     * @param propertyValues New values to replace the existing ones.
     */
    public abstract void replacePropertyColumn(String propertyName, List<Object> propertyValues);

    /**
     * Replaces all values for a specific pre-event trigger.
     *
     * @param preEventName  Name of the pre-event.
     * @param preEventValues New values to replace the existing ones.
     */
    public abstract void replacePreEventTrigger(String preEventName, List<Object> preEventValues);

    /**
     * Replaces all values for a specific post-event trigger.
     *
     * @param postEventName  Name of the post-event.
     * @param postEventValues New values to replace the existing ones.
     */
    public abstract void replacePostEventTrigger(String postEventName, List<Object> postEventValues);

    /**
     * Retrieves all values for a specific property column.
     *
     * @param propertyName Name of the property.
     * @return List of property values.
     */
    public abstract List<Object> getPropertyColumnAsList(String propertyName);

    /**
     * Retrieves all values for a specific pre-event column.
     *
     * @param preEventName Name of the pre-event.
     * @return List of pre-event values.
     */
    public abstract List<Object> getPreEventColumnAsList(String preEventName);

    /**
     * Retrieves all values for a specific post-event column.
     *
     * @param postEventName Name of the post-event.
     * @return List of post-event values.
     */
    public abstract List<Object> getPostEventColumnAsList(String postEventName);
}
