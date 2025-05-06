package agentarium.scheduler;

import agentarium.agents.AgentSet;

/**
 * Interface representing a scheduling policy for running a single tick
 * of the agent-based model.
 * <p>
 * Implementations of this interface define how a tick is executed over
 * a given set of agents.
 * </p>
 */
public interface ModelScheduler {

    /**
     * Executes a single simulation tick for the provided agent set.
     *
     * @param agentSet the set of agents to execute for this tick
     */
    void runTick(AgentSet agentSet);
}
