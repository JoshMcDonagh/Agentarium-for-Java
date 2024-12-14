package agents;

import agents.attributes.AgentAttributeResults;
import agents.attributes.AgentAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentResults {
    private String agentName;

    private List<AgentAttributeResults> agentAttributeResultsList = new ArrayList<AgentAttributeResults>();
    private Map<String, AgentAttributeResults> agentAttributeResultsMap = new HashMap<String, AgentAttributeResults>();

    public void setup(String agentName, List<AgentAttributeSet> attributes) {
        this.agentName = agentName;
        for (AgentAttributeSet attribute : attributes) {
            AgentAttributeResults agentAttributesResults = new AgentAttributeResults(agentName, attribute);
            agentAttributeResultsList.add(agentAttributesResults);
            agentAttributeResultsMap.put(agentAttributesResults.getAgentName(), agentAttributesResults);
        }
    }

    public AgentAttributeResults getAttributeResults(String attributeName) {
        return agentAttributeResultsMap.get(attributeName);
    }

    public String getAgentName() {
        return agentName;
    }
}
