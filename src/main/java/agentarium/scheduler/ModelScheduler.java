package agentarium.scheduler;

import agentarium.agents.AgentSet;

public interface ModelScheduler {
    void runTick(AgentSet agentSet);
}
