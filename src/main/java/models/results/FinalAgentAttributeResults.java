package models.results;

import agents.Agent;
import agents.attributes.results.AgentAttributeResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the final aggregated results of attributes for all agents in a simulation.
 * Provides mechanisms to retrieve and manipulate individual or collective agent results.
 */
public class FinalAgentAttributeResults {

    // Map linking each agent's name to their attribute results.
    private final Map<String, AgentAttributeResults> agentAttributeResultsMap = new HashMap<>();

    // List of all attribute results for agents, preserving their insertion order.
    private final List<AgentAttributeResults> agentAttributeResultsList = new ArrayList<>();

    /**
     * Constructs the `FinalAgentAttributeResults` object by aggregating results from a list of agents.
     *
     * @param agents A list of agents whose attribute results are to be aggregated.
     */
    public FinalAgentAttributeResults(List<Agent> agents) {
        for (Agent agent : agents) {
            // Store results in both map and list for quick access by name and index.
            agentAttributeResultsMap.put(agent.name(), agent.getResults());
            agentAttributeResultsList.add(agent.getResults());
        }
    }

    /**
     * Merges the results from another `FinalAgentAttributeResults` into this instance.
     *
     * @param other The other `FinalAgentAttributeResults` to merge with this one.
     */
    public void mergeWith(FinalAgentAttributeResults other) {
        // Combine the maps from both results.
        agentAttributeResultsMap.putAll(other.agentAttributeResultsMap);
    }

    /**
     * Retrieves the property values of a specific property for an agent's attribute.
     *
     * @param agentName    The name of the agent.
     * @param attributeName The name of the attribute.
     * @param propertyName  The name of the property.
     * @return A list of property values for the specified property.
     */
    public List<Object> getAgentPropertyValues(String agentName, String attributeName, String propertyName) {
        return agentAttributeResultsMap.get(agentName)
                .getAttributeResults(attributeName)
                .getPropertyValues(propertyName);
    }

    /**
     * Retrieves the pre-event trigger results for a specific event of an agent's attribute.
     *
     * @param agentName    The name of the agent.
     * @param attributeName The name of the attribute.
     * @param eventName     The name of the pre-event.
     * @return A list of booleans representing whether the event was triggered.
     */
    public List<Boolean> getAgentPreEventTriggers(String agentName, String attributeName, String eventName) {
        return agentAttributeResultsMap.get(agentName)
                .getAttributeResults(attributeName)
                .getPreEventTriggers(eventName);
    }

    /**
     * Retrieves the post-event trigger results for a specific event of an agent's attribute.
     *
     * @param agentName    The name of the agent.
     * @param attributeName The name of the attribute.
     * @param eventName     The name of the post-event.
     * @return A list of booleans representing whether the event was triggered.
     */
    public List<Boolean> getAgentPostEventTriggers(String agentName, String attributeName, String eventName) {
        return agentAttributeResultsMap.get(agentName)
                .getAttributeResults(attributeName)
                .getPostEventTriggers(eventName);
    }

    /**
     * Retrieves the attribute results for a specific agent by their name.
     *
     * @param agentName The name of the agent.
     * @return The `AgentAttributeResults` object for the specified agent.
     */
    public AgentAttributeResults getAgentAttributeResults(String agentName) {
        return agentAttributeResultsMap.get(agentName);
    }

    /**
     * Retrieves the attribute results for an agent by their index in the list.
     *
     * @param index The index of the agent in the list.
     * @return The `AgentAttributeResults` object at the specified index.
     */
    public AgentAttributeResults getAgentAttributeResultsByIndex(int index) {
        return agentAttributeResultsList.get(index);
    }

    /**
     * Retrieves the total number of attribute results stored.
     *
     * @return The number of agent attribute results.
     */
    public int getAgentAttributeResultsCount() {
        return agentAttributeResultsList.size();
    }

    /**
     * Disconnects all database connections associated with agent attribute results
     * and clears all stored data.
     */
    public void disconnectDatabases() {
        for (AgentAttributeResults agentAttributeResults : agentAttributeResultsList) {
            // Disconnect databases for each agent's results.
            agentAttributeResults.disconnectDatabases();
        }
        // Clear both map and list to free memory.
        agentAttributeResultsMap.clear();
        agentAttributeResultsList.clear();
    }
}
