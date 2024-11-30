package models;

import agents.AgentClock;

public class ModelClock {
    private final int totalNumOfTicksToRun;
    private final int numOfWarmUpTicks;

    public ModelClock(int totalNumOfTicksToRun, int numOfWarmUpTicks) {
        this.totalNumOfTicksToRun = totalNumOfTicksToRun;
        this.numOfWarmUpTicks = numOfWarmUpTicks;
    }

    public AgentClock generateAgentCLock() {
        return new AgentClock(totalNumOfTicksToRun, numOfWarmUpTicks);
    }

    public int getTotalNumOfTicksToRun() {
        return totalNumOfTicksToRun;
    }
}
