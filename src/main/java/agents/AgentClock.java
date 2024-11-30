package agents;

public class AgentClock {
    int ticksSinceStart = 0;
    int ticksSinceWarmUp = 0;

    int totalNumOfTicksToRun;
    int numOfWarmUpTicks;

    public AgentClock(int totalNumOfTicksToRun, int numOfWarmUpTicks) {
        this.totalNumOfTicksToRun = totalNumOfTicksToRun;
        this.numOfWarmUpTicks = numOfWarmUpTicks;
    }

    public boolean isWarmedUp() {
        return ticksSinceStart > numOfWarmUpTicks;
    }

    public int getTicksSinceStart() {
        return ticksSinceStart;
    }

    public int getTicksSinceWarmUp() {
        return ticksSinceWarmUp;
    }

    public int getTotalNumOfTicksToRun() {
        return totalNumOfTicksToRun;
    }

    public void tick() {
        ticksSinceStart++;
        if (isWarmedUp())
            ticksSinceWarmUp++;
    }
}
