package agentarium;

public class ModelClock {
    private final int numOfTicksToRun;
    private final int numOfWarmUpTicks;

    private int tick = 0;

    public ModelClock(int numOfTicksToRun, int numOfWarmUpTicks) {
        this.numOfTicksToRun = numOfTicksToRun;
        this.numOfWarmUpTicks = numOfWarmUpTicks;
    }

    public boolean isWarmingUp() {
        return tick < numOfWarmUpTicks;
    }

    public boolean isRunning() {
        return tick < numOfTicksToRun + numOfWarmUpTicks;
    }

    public int getTick() {
        return tick;
    }

    public void triggerTick() {
        if (isRunning())
            tick++;
    }
}
