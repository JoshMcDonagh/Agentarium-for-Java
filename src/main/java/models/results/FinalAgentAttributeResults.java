package models.results;

import agents.attributes.results.AgentAttributeResults;

import java.util.List;
import java.util.Map;

public class FinalAgentAttributeResults {
    private final Map<String, AgentAttributeResults> agentAttributeResultsMap;

    public FinalAgentAttributeResults(Map<String, AgentAttributeResults> agentAttributeResultsMap) {
        this.agentAttributeResultsMap = agentAttributeResultsMap;
    }

    public List<Object> getAgentPropertyValues(String agentName, String attributeName, String propertyName) {
        return agentAttributeResultsMap.get(agentName).getAttributeResults(attributeName).getPropertyValues(propertyName);
    }

    public List<Boolean> getAgentPreEventTriggers(String agentName, String attributeName, String eventName) {
        return agentAttributeResultsMap.get(agentName).getAttributeResults(attributeName).getPreEventTriggers(eventName);
    }

    public List<Boolean> getAgentPostEventTriggers(String agentName, String attributeName, String eventName) {
        return agentAttributeResultsMap.get(agentName).getAttributeResults(attributeName).getPostEventTriggers(eventName);
    }
}
