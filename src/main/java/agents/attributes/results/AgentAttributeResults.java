package agents.attributes.results;

import agents.attributes.AgentAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentAttributeResults {
    private String agentName;

    private final List<AgentAttributeSetResults> agentAttributeResultsList = new ArrayList<AgentAttributeSetResults>();
    private final Map<String, AgentAttributeSetResults> agentAttributeResultsMap = new HashMap<String, AgentAttributeSetResults>();

    public void setup(String agentName, List<AgentAttributeSet> attributesSets) {
        this.agentName = agentName;
        for (AgentAttributeSet attributeSet : attributesSets) {
            AgentAttributeSetResults agentAttributesResults = new AgentAttributeSetResults(agentName, attributeSet);
            agentAttributeResultsList.add(agentAttributesResults);
            agentAttributeResultsMap.put(attributeSet.name(), agentAttributesResults);
        }
    }

    public AgentAttributeSetResults getAttributeResults(String attributeSetName) {
        return agentAttributeResultsMap.get(attributeSetName);
    }

    public AgentAttributeSetResults getAttributeResultsByIndex(int index) {
        return agentAttributeResultsList.get(index);
    }

    public int getAttributeSetCount() {
        return agentAttributeResultsList.size();
    }

    public String getAgentName() {
        return agentName;
    }

    public void disconnectDatabases() {
        for (AgentAttributeSetResults agentAttributeSetResults : agentAttributeResultsList)
            agentAttributeSetResults.disconnectDatabase();
        agentAttributeResultsMap.clear();
        agentAttributeResultsList.clear();
    }
}
