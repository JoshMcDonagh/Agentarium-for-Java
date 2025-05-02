package agentarium.results;

import agentarium.ModelElement;
import agentarium.agents.AgentSet;

import java.util.List;

public class AgentResults extends ModelElementResults {
    public AgentResults(AgentSet agentSet) {
        super(agentSet.getAsList());
    }
}
