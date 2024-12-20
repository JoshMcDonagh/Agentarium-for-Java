package agents;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AgentClock} class.
 * Verifies the functionality of time tracking, warm-up status, and tick progression.
 */
class AgentClockTest {

    private AgentClock agentClock;

    /**
     * Sets up an `AgentClock` instance with predefined total ticks and warm-up ticks before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialise the AgentClock with 100 total ticks and 10 warm-up ticks.
        agentClock = new AgentClock(100, 10);
    }

    /**
     * Tests that the clock initially reports that the warm-up period has not been reached.
     */
    @Test
    void testIsWarmedUpInitially() {
        assertFalse(agentClock.isWarmedUp(), "Clock should not be warmed up initially.");
    }

    /**
     * Tests the warm-up status after surpassing the warm-up period.
     */
    @Test
    void testIsWarmedUpAfterWarmUpPeriod() {
        // Simulate 11 ticks to surpass the warm-up period.
        for (int i = 0; i < 11; i++) {
            agentClock.tick();
        }
        assertTrue(agentClock.isWarmedUp(), "Clock should be warmed up after exceeding warm-up ticks.");
    }

    /**
     * Tests the retrieval of ticks since the clock started.
     */
    @Test
    void testGetTicksSinceStart() {
        // Simulate 5 ticks.
        for (int i = 0; i < 5; i++) {
            agentClock.tick();
        }
        assertEquals(5, agentClock.getTicksSinceStart(), "Ticks since start should be 5.");
    }

    /**
     * Tests the retrieval of ticks since the warm-up period.
     */
    @Test
    void testGetTicksSinceWarmUp() {
        // Simulate 15 ticks (5 after the warm-up period).
        for (int i = 0; i < 15; i++) {
            agentClock.tick();
        }
        assertEquals(5, agentClock.getTicksSinceWarmUp(), "Ticks since warm-up should be 5.");
    }

    /**
     * Tests that no ticks are counted for the warm-up period initially.
     */
    @Test
    void testTicksSinceWarmUpBeforeWarmUpPeriod() {
        // Simulate 5 ticks (all within the warm-up period).
        for (int i = 0; i < 5; i++) {
            agentClock.tick();
        }
        assertEquals(0, agentClock.getTicksSinceWarmUp(), "Ticks since warm-up should be 0 before warm-up ends.");
    }

    /**
     * Tests the retrieval of the total number of ticks the clock is set to run.
     */
    @Test
    void testGetTotalNumOfTicksToRun() {
        assertEquals(100, agentClock.getTotalNumOfTicksToRun(), "Total ticks to run should be 100.");
    }

    /**
     * Tests the progression of the clock's tick count.
     */
    @Test
    void testTickIncrements() {
        // Simulate 1 tick.
        agentClock.tick();
        assertEquals(1, agentClock.getTicksSinceStart(), "Ticks since start should increment by 1.");
        assertEquals(0, agentClock.getTicksSinceWarmUp(), "Ticks since warm-up should remain 0.");
    }
}
