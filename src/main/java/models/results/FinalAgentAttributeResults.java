package models.results;

import agents.Agent;
import agents.attributes.results.AgentAttributeResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalAgentAttributeResults {
    private final Map<String, AgentAttributeResults> agentAttributeResultsMap = new HashMap<String, AgentAttributeResults>();
    private final List<AgentAttributeResults> agentAttributeResultsList = new ArrayList<AgentAttributeResults>();

    public FinalAgentAttributeResults(List<Agent> agents) {
        for (Agent agent : agents) {
            agentAttributeResultsMap.put(agent.name(), agent.getResults());
            agentAttributeResultsList.add(agent.getResults());
        }
    }

    public void mergeWith(FinalAgentAttributeResults other) {
        agentAttributeResultsMap.putAll(other.agentAttributeResultsMap);
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

    public AgentAttributeResults getAgentAttributeResults(String agentName) {
        return agentAttributeResultsMap.get(agentName);
    }

    public AgentAttributeResults getAgentAttributeResultsByIndex(int index) {
        return agentAttributeResultsList.get(index);
    }

    public int getAgentAttributeResultsCount() {
        return agentAttributeResultsList.size();
    }

    public void disconnectDatabases() {
        for (AgentAttributeResults agentAttributeResults : agentAttributeResultsList)
            agentAttributeResults.disconnectDatabases();
        agentAttributeResultsMap.clear();
        agentAttributeResultsList.clear();
    }
}
