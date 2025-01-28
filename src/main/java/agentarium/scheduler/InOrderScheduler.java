package agentarium.scheduler;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;

public class InOrderScheduler implements ModelScheduler {

    @Override
    public void runTick(AgentSet agentSet) {
        for (Agent agent : agentSet)
            agent.run();
    }
}
