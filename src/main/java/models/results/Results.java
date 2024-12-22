package models.results;

import agents.Agent;
import agents.attributes.results.AgentAttributeResults;
import agents.attributes.results.AgentAttributeSetResults;
import attributedatabases.AttributeResultsDatabase;
import attributedatabases.AttributeResultsDatabaseFactory;
import models.modelattributes.results.ModelAttributeResults;
import models.modelattributes.results.ModelAttributeSetResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class managing the results of agent and model attributes in simulations.
 * Supports aggregation, retrieval, and processing of attribute results,
 * as well as managing database connections for raw and accumulated data.
 */
public abstract class Results {

    // Final results for agents and model attributes
    private FinalAgentAttributeResults finalAgentAttributeResults;
    private FinalModelAttributeResults finalModelAttributeResults;

    // Maps and lists for accumulated databases
    private final Map<String, AttributeResultsDatabase> accumulatedAgentAttributeResultsDatabasesMap = new HashMap<String, AttributeResultsDatabase>();
    private final Map<String, AttributeResultsDatabase> accumulatedModelAttributeResultsDatabasesMap = new HashMap<String, AttributeResultsDatabase>();
    private final List<AttributeResultsDatabase> accumulatedAgentAttributeResultsDatabasesList = new ArrayList<AttributeResultsDatabase>();
    private final List<AttributeResultsDatabase> accumulatedModelAttributeResultsDatabasesList = new ArrayList<AttributeResultsDatabase>();

    // List of agent names
    private final List<String> agentNames = new ArrayList<>();

    // Flags to track database connection states
    private boolean isRawAgentAttributeDataConnected = false;
    private boolean isRawModelAttributeDataConnected = false;
    private boolean isAccumulatedModelAttributeDataConnected = false;

    /**
     * Sets the names of the agents for tracking purposes.
     *
     * @param agents List of agents whose names are to be stored.
     */
    public void setAgentNames(List<Agent> agents) {
        for (Agent agent : agents)
            agentNames.add(agent.name());
    }

    /**
     * Retrieves the list of agent names.
     *
     * @return A list of agent names.
     */
    public List<String> getAgentNames() {
        return agentNames;
    }

    /**
     * Sets the final results for agent attributes.
     *
     * @param finalAgentAttributeResults The results to set.
     */
    public void setFinalAgentAttributeResults(FinalAgentAttributeResults finalAgentAttributeResults) {
        this.finalAgentAttributeResults = finalAgentAttributeResults;
        if (finalAgentAttributeResults != null)
            isRawAgentAttributeDataConnected = true;
    }

    /**
     * Sets the final results for model attributes.
     *
     * @param finalModelAttributeResults The results to set.
     */
    public void setFinalModelAttributes(FinalModelAttributeResults finalModelAttributeResults) {
        this.finalModelAttributeResults = finalModelAttributeResults;
        if (finalModelAttributeResults != null)
            isRawModelAttributeDataConnected = true;
    }

    /**
     * Retrieves the property values of a specific property for an agent.
     *
     * @param agentName    The name of the agent.
     * @param attributeName The name of the attribute.
     * @param propertyName  The name of the property.
     * @return A list of property values.
     * @throws IllegalStateException if the raw agent attribute database is disconnected.
     */
    public List<Object> getAgentPropertyValues(String agentName, String attributeName, String propertyName) {
        if (isRawAgentAttributeDataConnected)
            return finalAgentAttributeResults.getAgentPropertyValues(agentName, attributeName, propertyName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the pre-event values of a specific pre-event for an agent.
     *
     * @param agentName    The name of the agent.
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of event values.
     * @throws IllegalStateException if the raw agent attribute database is disconnected.
     */
    public List<Boolean> getAgentPreEventTriggers(String agentName, String attributeName, String eventName) {
        if (isRawAgentAttributeDataConnected)
            return finalAgentAttributeResults.getAgentPreEventTriggers(agentName, attributeName, eventName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the post-event values of a specific post-event for an agent.
     *
     * @param agentName    The name of the agent.
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of event values.
     * @throws IllegalStateException if the raw agent attribute database is disconnected.
     */
    public List<Boolean> getAgentPostEventTriggers(String agentName, String attributeName, String eventName) {
        if (isRawAgentAttributeDataConnected)
            return finalAgentAttributeResults.getAgentPostEventTriggers(agentName, attributeName, eventName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the property values of a specific property for the model.
     *
     * @param attributeName The name of the attribute.
     * @param propertyName  The name of the property.
     * @return A list of property values.
     * @throws IllegalStateException if the raw model attribute database is disconnected.
     */
    public List<Object> getModelPropertyValues(String attributeName, String propertyName) {
        if (isRawModelAttributeDataConnected)
            return finalModelAttributeResults.getModelPropertyValues(attributeName, propertyName);
        else
            throw new IllegalStateException("Access of model attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the pre-event values of a specific pre-event for the model.
     *
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of event values.
     * @throws IllegalStateException if the raw model attribute database is disconnected.
     */
    public List<Boolean> getModelPreEventTriggers(String attributeName, String eventName) {
        if (isRawModelAttributeDataConnected)
            return finalModelAttributeResults.getModelPreEventTriggers(attributeName, eventName);
        else
            throw new IllegalStateException("Access of model attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the post-event values of a specific post-event for the model.
     *
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of event values.
     * @throws IllegalStateException if the raw model attribute database is disconnected.
     */
    public List<Boolean> getModelPostEventTriggers(String attributeName, String eventName) {
        if (isRawModelAttributeDataConnected)
            return finalModelAttributeResults.getModelPostEventTriggers(attributeName, eventName);
        else
            throw new IllegalStateException("Access of model attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the final overall property values of a specific property for agents.
     *
     * @param attributeName The name of the attribute.
     * @param propertyName  The name of the property.
     * @return A list of accumulated property values.
     * @throws IllegalStateException if the accumulated agent attribute database is disconnected.
     */
    public List<Object> getAccumulatedAgentPropertyValues(String attributeName, String propertyName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPropertyColumnAsList(propertyName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the final overall pre-event values of a specific pre-event for agents.
     *
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of accumulated event values.
     * @throws IllegalStateException if the accumulated agent attribute database is disconnected.
     */
    public List<Object> getAccumulatedAgentPreEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPreEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the final overall post-event values of a specific post-event for agents.
     *
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of accumulated event values.
     * @throws IllegalStateException if the accumulated agent attribute database is disconnected.
     */
    public List<Object> getAccumulatedAgentPostEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPostEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the final overall property values of a specific property for the model.
     *
     * @param attributeName The name of the attribute.
     * @param propertyName  The name of the property.
     * @return A list of accumulated property values.
     * @throws IllegalStateException if the accumulated model attribute database is disconnected.
     */
    public List<Object> getAccumulatedModelPropertyValues(String attributeName, String propertyName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedModelAttributeResultsDatabasesMap.get(attributeName).getPropertyColumnAsList(propertyName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the final overall pre-event values of a specific pre-event for the model.
     *
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of accumulated event values.
     * @throws IllegalStateException if the accumulated model attribute database is disconnected.
     */
    public List<Object> getAccumulatedModelPreEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedModelAttributeResultsDatabasesMap.get(attributeName).getPreEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Retrieves the final overall post-event values of a specific post-event for the model.
     *
     * @param attributeName The name of the attribute.
     * @param eventName  The name of the event.
     * @return A list of accumulated event values.
     * @throws IllegalStateException if the accumulated model attribute database is disconnected.
     */
    public List<Object> getAccumulatedModelPostEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedModelAttributeResultsDatabasesMap.get(attributeName).getPostEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    /**
     * Disconnects all raw databases for agents and models.
     */
    public void disconnectRawDatabases() {
        if (isRawAgentAttributeDataConnected) {
            finalAgentAttributeResults.disconnectDatabases();
            isRawAgentAttributeDataConnected = false;
        }

        if (isRawModelAttributeDataConnected) {
            finalModelAttributeResults.disconnectDatabases();
            isRawModelAttributeDataConnected = false;
        }
    }

    /**
     * Disconnects all accumulated databases for agents and models.
     */
    public void disconnectAccumulatedDatabases() {
        if (isAccumulatedModelAttributeDataConnected) {
            for (AttributeResultsDatabase attributeResultsDatabase : accumulatedAgentAttributeResultsDatabasesList)
                attributeResultsDatabase.disconnect();
            accumulatedModelAttributeResultsDatabasesMap.clear();
            accumulatedAgentAttributeResultsDatabasesList.clear();

            for (AttributeResultsDatabase attributeResultsDatabase : accumulatedModelAttributeResultsDatabasesList)
                attributeResultsDatabase.disconnect();
            accumulatedModelAttributeResultsDatabasesMap.clear();
            accumulatedModelAttributeResultsDatabasesList.clear();

            isAccumulatedModelAttributeDataConnected = false;
        }
    }

    /**
     * Disconnects all databases for agents and models.
     */
    public void disconnectAllDatabases() {
        disconnectRawDatabases();
        disconnectAccumulatedDatabases();
    }

    /**
     * Accumulates agent attribute data from raw results into databases.
     */
    public void accumulateAgentAttributeData() {
        for (int i = 0; i < finalAgentAttributeResults.getAgentAttributeResultsCount(); i++) {
            // Retrieve results for the current agent.
            AgentAttributeResults agentAttributeResults = finalAgentAttributeResults.getAgentAttributeResultsByIndex(i);

            // Initialise databases if this is the first call.
            if (accumulatedAgentAttributeResultsDatabasesMap.isEmpty()) {
                for (int j = 0; j < agentAttributeResults.getAttributeSetCount(); j++) {
                    String attributeName = agentAttributeResults.getAttributeResultsByIndex(j).getAttributeName();
                    AttributeResultsDatabase newDatabase = AttributeResultsDatabaseFactory.createDatabase();
                    assert newDatabase != null;
                    newDatabase.connect();
                    accumulatedAgentAttributeResultsDatabasesMap.put(attributeName, newDatabase);
                    accumulatedAgentAttributeResultsDatabasesList.add(newDatabase);
                }
                // Mark that the accumulated data is connected.
                isAccumulatedModelAttributeDataConnected = true;
            }

            // Process attributes for the current agent.
            for (int j = 0; j < agentAttributeResults.getAttributeSetCount(); j++) {
                AgentAttributeSetResults agentAttributeSetResults = agentAttributeResults.getAttributeResultsByIndex(j);
                String attributeName = agentAttributeSetResults.getAttributeName();

                // Process property values for this attribute.
                List<String> propertyNamesList = agentAttributeSetResults.getPropertyNamesList();
                for (String propertyName : propertyNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPropertyColumnAsList(propertyName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPropertyValues(propertyName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, propertyName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).setPropertyColumn(propertyName, (List<Object>) newAccumulatedValues);
                }

                // Process pre-event triggers for this attribute.
                List<String> preEventNamesList = agentAttributeSetResults.getPreEventNamesList();
                for (String preEventName : preEventNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPreEventColumnAsList(preEventName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPreEventTriggers(preEventName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, preEventName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).setPreEventColumn(preEventName, (List<Object>) newAccumulatedValues);
                }

                // Process post-event triggers for this attribute.
                List<String> postEventNamesList = agentAttributeSetResults.getPostEventNamesList();
                for (String postEventName : postEventNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPostEventColumnAsList(postEventName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPostEventTriggers(postEventName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, postEventName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).setPostEventColumn(postEventName, (List<Object>) newAccumulatedValues);
                }
            }
        }
    }

    /**
     * Processes model attribute data from raw results into accumulated databases.
     */
    public void processModelAttributeData() {
        // Retrieve the results of all model attributes.
        ModelAttributeResults modelAttributeResults = finalModelAttributeResults.get();

        // Check if the accumulated model attribute results database map is empty.
        // If empty, initialise the databases for each attribute.
        if (accumulatedModelAttributeResultsDatabasesMap.isEmpty()) {
            for (int j = 0; j < modelAttributeResults.getAttributeSetCount(); j++) {
                // Get the name of the current attribute.
                String attributeName = modelAttributeResults.getAttributeResultsByIndex(j).getAttributeName();

                // Create and connect a new database for the attribute.
                AttributeResultsDatabase newDatabase = AttributeResultsDatabaseFactory.createDatabase();
                assert newDatabase != null; // Ensure the database was created successfully.
                newDatabase.connect();

                // Store the database in the map and add it to the list.
                accumulatedModelAttributeResultsDatabasesMap.put(attributeName, newDatabase);
                accumulatedModelAttributeResultsDatabasesList.add(newDatabase);
            }
        }

        // Iterate over each model attribute to process its data.
        for (int i = 0; i < modelAttributeResults.getAttributeSetCount(); i++) {
            // Retrieve the results for the current attribute set.
            ModelAttributeSetResults modelAttributeSetResults = modelAttributeResults.getAttributeResultsByIndex(i);
            String attributeName = modelAttributeSetResults.getAttributeName();

            // Process and store property values for the attribute.
            List<String> propertyNamesList = modelAttributeSetResults.getPropertyNamesList();
            for (String propertyName : propertyNamesList) {
                // Get the property values to be processed.
                List<?> valuesToBeProcessed = modelAttributeSetResults.getPropertyValues(propertyName);

                // Process the property values and store the results in the database.
                List<?> newProcessedValues = processModelPropertyResults(attributeName, propertyName, valuesToBeProcessed);
                accumulatedModelAttributeResultsDatabasesMap.get(attributeName).setPropertyColumn(propertyName, (List<Object>) newProcessedValues);
            }

            // Process and store pre-event triggers for the attribute.
            List<String> preEventNamesList = modelAttributeSetResults.getPreEventNamesList();
            for (String preEventName : preEventNamesList) {
                // Get the pre-event triggers to be processed.
                List<?> valuesToBeProcessed = modelAttributeSetResults.getPreEventTriggers(preEventName);

                // Process the pre-event triggers and store the results in the database.
                List<?> newProcessedValues = processModelPreEventResults(attributeName, preEventName, valuesToBeProcessed);
                accumulatedModelAttributeResultsDatabasesMap.get(attributeName).setPreEventColumn(preEventName, (List<Object>) newProcessedValues);
            }

            // Process and store post-event triggers for the attribute.
            List<String> postEventNamesList = modelAttributeSetResults.getPostEventNamesList();
            for (String postEventName : postEventNamesList) {
                // Get the post-event triggers to be processed.
                List<?> valuesToBeProcessed = modelAttributeSetResults.getPostEventTriggers(postEventName);

                // Process the post-event triggers and store the results in the database.
                List<?> newProcessedValues = processModelPostEventResults(attributeName, postEventName, valuesToBeProcessed);
                accumulatedModelAttributeResultsDatabasesMap.get(attributeName).setPostEventColumn(postEventName, (List<Object>) newProcessedValues);
            }
        }
    }

    /**
     * Merges results with another `Results` object before accumulation.
     *
     * @param otherResults The results to merge with.
     */
    public void mergeWithBeforeAccumulation(Results otherResults) {
        finalAgentAttributeResults.mergeWith(otherResults.finalAgentAttributeResults);
    }

    /**
     * Processes the property values of a model attribute.
     * Subclasses can override this method to apply custom transformations to the property values.
     *
     * @param attributeName The name of the attribute being processed.
     * @param propertyName  The name of the property within the attribute.
     * @param propertyValues The original property values to process.
     * @return The processed property values (default behaviour returns the original values).
     */
    protected List<?> processModelPropertyResults(String attributeName, String propertyName, List<?> propertyValues) {
        // Default implementation: return the original property values without modification.
        return propertyValues;
    }

    /**
     * Processes the pre-event triggers of a model attribute.
     * Subclasses can override this method to apply custom transformations to the pre-event values.
     *
     * @param attributeName The name of the attribute being processed.
     * @param eventName     The name of the pre-event within the attribute.
     * @param preEventValues The original pre-event trigger values to process.
     * @return The processed pre-event values (default behaviour returns the original values).
     */
    protected List<?> processModelPreEventResults(String attributeName, String eventName, List<?> preEventValues) {
        // Default implementation: return the original pre-event values without modification.
        return preEventValues;
    }

    /**
     * Processes the post-event triggers of a model attribute.
     * Subclasses can override this method to apply custom transformations to the post-event values.
     *
     * @param attributeName The name of the attribute being processed.
     * @param eventName     The name of the post-event within the attribute.
     * @param postEventValues The original post-event trigger values to process.
     * @return The processed post-event values (default behaviour returns the original values).
     */
    protected List<?> processModelPostEventResults(String attributeName, String eventName, List<?> postEventValues) {
        // Default implementation: return the original post-event values without modification.
        return postEventValues;
    }

    // Abstract methods for accumulating results

    /**
     * Accumulates property values for an agent attribute across multiple data points.
     * Subclasses must implement this method to define how property values are aggregated.
     *
     * @param attributeName     The name of the attribute being processed.
     * @param propertyName      The name of the property within the attribute.
     * @param accumulatedValues The previously accumulated property values.
     * @param valuesToBeProcessed The new property values to add to the accumulation.
     * @return The updated list of accumulated property values.
     */
    protected abstract List<?> accumulateAgentPropertyResults(
            String attributeName,
            String propertyName,
            List<?> accumulatedValues,
            List<?> valuesToBeProcessed
    );

    /**
     * Accumulates pre-event trigger values for an agent attribute across multiple data points.
     * Subclasses must implement this method to define how pre-event values are aggregated.
     *
     * @param attributeName     The name of the attribute being processed.
     * @param preEventName      The name of the pre-event within the attribute.
     * @param accumulatedValues The previously accumulated pre-event trigger values.
     * @param valuesToBeProcessed The new pre-event trigger values to add to the accumulation.
     * @return The updated list of accumulated pre-event trigger values.
     */
    protected abstract List<?> accumulateAgentPreEventResults(
            String attributeName,
            String preEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed
    );

    /**
     * Accumulates post-event trigger values for an agent attribute across multiple data points.
     * Subclasses must implement this method to define how post-event values are aggregated.
     *
     * @param attributeName     The name of the attribute being processed.
     * @param postEventName     The name of the post-event within the attribute.
     * @param accumulatedValues The previously accumulated post-event trigger values.
     * @param valuesToBeProcessed The new post-event trigger values to add to the accumulation.
     * @return The updated list of accumulated post-event trigger values.
     */
    protected abstract List<?> accumulateAgentPostEventResults(
            String attributeName,
            String postEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed
    );
}