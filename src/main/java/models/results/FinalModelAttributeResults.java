package models.results;

import models.modelattributes.ModelAttributeSet;
import models.modelattributes.results.ModelAttributeResults;

import java.util.List;

/**
 * Manages the final aggregated results of model attributes in a simulation.
 * Provides access to property values, pre-event triggers, post-event triggers,
 * and allows for cleanup of database connections.
 */
public class FinalModelAttributeResults {

    // Holds the results of all model attributes.
    private final ModelAttributeResults modelAttributeResults;

    /**
     * Constructs the `FinalModelAttributeResults` using a list of model attribute sets.
     *
     * @param modelAttributeSet The list of model attribute sets to initialise results.
     */
    public FinalModelAttributeResults(List<ModelAttributeSet> modelAttributeSet) {
        // Initialise the model attribute results with the given attribute sets.
        this.modelAttributeResults = new ModelAttributeResults(modelAttributeSet);
    }

    /**
     * Retrieves the property values of a specific property for a model attribute.
     *
     * @param attributeName The name of the model attribute.
     * @param propertyName  The name of the property.
     * @return A list of property values for the specified property.
     */
    public List<Object> getModelPropertyValues(String attributeName, String propertyName) {
        return modelAttributeResults
                .getAttributeResults(attributeName)
                .getPropertyValues(propertyName);
    }

    /**
     * Retrieves the pre-event trigger results for a specific event of a model attribute.
     *
     * @param attributeName The name of the model attribute.
     * @param eventName     The name of the pre-event.
     * @return A list of booleans representing whether the event was triggered.
     */
    public List<Boolean> getModelPreEventTriggers(String attributeName, String eventName) {
        return modelAttributeResults
                .getAttributeResults(attributeName)
                .getPreEventTriggers(eventName);
    }

    /**
     * Retrieves the post-event trigger results for a specific event of a model attribute.
     *
     * @param attributeName The name of the model attribute.
     * @param eventName     The name of the post-event.
     * @return A list of booleans representing whether the event was triggered.
     */
    public List<Boolean> getModelPostEventTriggers(String attributeName, String eventName) {
        return modelAttributeResults
                .getAttributeResults(attributeName)
                .getPostEventTriggers(eventName);
    }

    /**
     * Retrieves the underlying `ModelAttributeResults` object.
     *
     * @return The `ModelAttributeResults` object.
     */
    public ModelAttributeResults get() {
        return modelAttributeResults;
    }

    /**
     * Disconnects all database connections associated with the model attribute results.
     */
    public void disconnectDatabases() {
        // Close database connections and clean up associated resources.
        modelAttributeResults.disconnectDatabases();
    }
}
