package agents;

import agents.attributes.AgentAttributeSetResults;
import agents.attributes.AgentAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentResults {
    private String agentName;

    private final List<AgentAttributeSetResults> agentAttributeResultsList = new ArrayList<AgentAttributeSetResults>();
    private final Map<String, AgentAttributeSetResults> agentAttributeResultsMap = new HashMap<String, AgentAttributeSetResults>();

    public void setup(String agentName, List<AgentAttributeSet> attributes) {
        this.agentName = agentName;
        for (AgentAttributeSet attribute : attributes) {
            AgentAttributeSetResults agentAttributesResults = new AgentAttributeSetResults(agentName, attribute);
            agentAttributeResultsList.add(agentAttributesResults);
            agentAttributeResultsMap.put(agentAttributesResults.getAgentName(), agentAttributesResults);
        }
    }

    public AgentAttributeSetResults getAttributeResults(String attributeName) {
        return agentAttributeResultsMap.get(attributeName);
    }

    public AgentAttributeSetResults getAttributeResultsByIndex(int index) {
        return agentAttributeResultsList.get(index);
    }

    public String getAgentName() {
        return agentName;
    }
}
