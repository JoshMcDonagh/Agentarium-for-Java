package agents;

/**
 * The `AgentClock` class is responsible for tracking time progression in terms of ticks.
 * It manages total ticks, warm-up ticks, and determines whether the warm-up period has completed.
 */
public class AgentClock {

    // The number of ticks since the clock started.
    private int ticksSinceStart = 0;

    // The number of ticks since the warm-up period ended.
    private int ticksSinceWarmUp = 0;

    // The total number of ticks the clock is expected to run.
    private final int totalNumOfTicksToRun;

    // The number of ticks designated as the warm-up period.
    private final int numOfWarmUpTicks;

    /**
     * Constructs an `AgentClock` with a specified total runtime and warm-up period.
     *
     * @param totalNumOfTicksToRun The total number of ticks the clock will run for.
     * @param numOfWarmUpTicks     The number of ticks allocated for the warm-up period.
     */
    public AgentClock(int totalNumOfTicksToRun, int numOfWarmUpTicks) {
        this.totalNumOfTicksToRun = totalNumOfTicksToRun;
        this.numOfWarmUpTicks = numOfWarmUpTicks;
    }

    /**
     * Checks if the clock has surpassed the warm-up period.
     *
     * @return `true` if the number of ticks since the start exceeds the warm-up ticks, otherwise `false`.
     */
    public boolean isWarmedUp() {
        return ticksSinceStart > numOfWarmUpTicks;
    }

    /**
     * Retrieves the total number of ticks since the clock started.
     *
     * @return The number of ticks since the clock's start.
     */
    public int getTicksSinceStart() {
        return ticksSinceStart;
    }

    /**
     * Retrieves the number of ticks that have occurred since the warm-up period ended.
     *
     * @return The number of ticks since the warm-up period.
     */
    public int getTicksSinceWarmUp() {
        return ticksSinceWarmUp;
    }

    /**
     * Retrieves the total number of ticks the clock is set to run.
     *
     * @return The total number of ticks to run.
     */
    public int getTotalNumOfTicksToRun() {
        return totalNumOfTicksToRun;
    }

    /**
     * Advances the clock by one tick. If the warm-up period has completed, the
     * ticks since warm-up are also incremented.
     */
    public void tick() {
        ticksSinceStart++;
        // Increment ticks since warm-up only if the warm-up period has passed.
        if (isWarmedUp()) {
            ticksSinceWarmUp++;
        }
    }
}
