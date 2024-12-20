package agents.attributes.results;

import agents.attributes.AgentAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the results of an agent's attributes over time.
 * This class manages the results for multiple attribute sets associated with an agent and provides access to those results by name or index.
 */
public class AgentAttributeResults {

    // The name of the agent these results are associated with.
    private String agentName;

    // A list containing the results for all attribute sets, in order of addition.
    private final List<AgentAttributeSetResults> agentAttributeResultsList = new ArrayList<>();

    // A map for quick access to results by attribute set name.
    private final Map<String, AgentAttributeSetResults> agentAttributeResultsMap = new HashMap<>();

    /**
     * Initialises the results for the agent and its associated attribute sets.
     *
     * @param agentName      The name of the agent.
     * @param attributesSets A list of attribute sets for which results should be managed.
     */
    public void setup(String agentName, List<AgentAttributeSet> attributesSets) {
        this.agentName = agentName;

        // Create results for each attribute set and add them to the list and map.
        for (AgentAttributeSet attributeSet : attributesSets) {
            AgentAttributeSetResults agentAttributesResults = new AgentAttributeSetResults(agentName, attributeSet);
            agentAttributeResultsList.add(agentAttributesResults);
            agentAttributeResultsMap.put(attributeSet.name(), agentAttributesResults);
        }
    }

    /**
     * Retrieves the results for a specific attribute set by name.
     *
     * @param attributeSetName The name of the attribute set.
     * @return The results for the specified attribute set, or {@code null} if no such set exists.
     */
    public AgentAttributeSetResults getAttributeResults(String attributeSetName) {
        return agentAttributeResultsMap.get(attributeSetName);
    }

    /**
     * Retrieves the results for a specific attribute set by its index in the list.
     *
     * @param index The index of the attribute set.
     * @return The results for the attribute set at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public AgentAttributeSetResults getAttributeResultsByIndex(int index) {
        return agentAttributeResultsList.get(index);
    }

    /**
     * Retrieves the number of attribute sets managed by this results object.
     *
     * @return The number of attribute sets.
     */
    public int getAttributeSetCount() {
        return agentAttributeResultsList.size();
    }

    /**
     * Retrieves the name of the agent these results are associated with.
     *
     * @return The name of the agent.
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Disconnects all databases associated with the agent's attribute sets and clears the results.
     * This method ensures that no lingering connections remain and prepares the object for reuse.
     */
    public void disconnectDatabases() {
        // Disconnect each attribute set's database.
        for (AgentAttributeSetResults agentAttributeSetResults : agentAttributeResultsList) {
            agentAttributeSetResults.disconnectDatabase();
        }

        // Clear the list and map to free resources.
        agentAttributeResultsMap.clear();
        agentAttributeResultsList.clear();
    }
}
