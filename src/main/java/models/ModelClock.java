package models;

import agents.AgentClock;

/**
 * Represents the clock used in a simulation model to manage and track the
 * total number of ticks and warm-up ticks.
 *
 * The `ModelClock` class is responsible for creating agent-specific clocks
 * and providing simulation runtime information.
 */
public class ModelClock {
    // Total number of ticks the simulation is configured to run.
    private final int totalNumOfTicksToRun;

    // Number of warm-up ticks to execute before the main simulation starts.
    private final int numOfWarmUpTicks;

    /**
     * Constructs a `ModelClock` with the specified total ticks and warm-up ticks.
     *
     * @param totalNumOfTicksToRun The total number of ticks the simulation should run.
     * @param numOfWarmUpTicks     The number of warm-up ticks before the main simulation.
     */
    public ModelClock(int totalNumOfTicksToRun, int numOfWarmUpTicks) {
        this.totalNumOfTicksToRun = totalNumOfTicksToRun;
        this.numOfWarmUpTicks = numOfWarmUpTicks;
    }

    /**
     * Generates an `AgentClock` for use by individual agents in the simulation.
     *
     * The `AgentClock` created will have the same configuration as this `ModelClock`,
     * ensuring consistency across all agents.
     *
     * @return A new `AgentClock` instance configured with the same number of ticks.
     */
    public AgentClock generateAgentCLock() {
        return new AgentClock(totalNumOfTicksToRun, numOfWarmUpTicks);
    }

    /**
     * Retrieves the total number of ticks the simulation is set to run.
     *
     * @return The total number of ticks.
     */
    public int getTotalNumOfTicksToRun() {
        return totalNumOfTicksToRun;
    }

    /**
     * Retrieves the number of warm-up ticks for the simulation.
     *
     * @return The number of warm-up ticks.
     */
    public int getNumOfWarmUpTicks() {
        return numOfWarmUpTicks;
    }
}
