package agentarium.agents;

import agentarium.ModelSettings;
import agentarium.attributes.AttributeSetCollection;

public class DefaultAgentGenerator extends agentarium.agents.AgentGenerator {
    private static int agentCount = 0;

    @Override
    protected Agent generateAgent(ModelSettings modelSettings) {
        AttributeSetCollection agentAttributeSetCollection = modelSettings.getBaseAgentAttributeSetCollection().deepCopyDuplicate();
        Agent newAgent = new Agent("Agent_" + agentCount, agentAttributeSetCollection);
        agentCount++;
        return newAgent;
    }
}
