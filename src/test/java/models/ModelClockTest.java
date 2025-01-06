package models;

import agents.AgentClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ModelClock} class.
 * Verifies the correctness of its methods and ensures proper functionality.
 */
class ModelClockTest {

    private ModelClock modelClock;

    @BeforeEach
    void setUp() {
        // Initialise ModelClock with 100 total ticks and 10 warm-up ticks.
        modelClock = new ModelClock(100, 10);
    }

    /**
     * Tests the retrieval of the total number of ticks to run.
     */
    @Test
    void testGetTotalNumOfTicksToRun() {
        int totalTicks = modelClock.getTotalNumOfTicksToRun();
        assertEquals(100, totalTicks, "Total number of ticks should match the value provided in the constructor.");
    }

    /**
     * Tests the retrieval of the number of warm-up ticks.
     */
    @Test
    void testGetNumOfWarmUpTicks() {
        int warmUpTicks = modelClock.getNumOfWarmUpTicks();
        assertEquals(10, warmUpTicks, "Number of warm-up ticks should match the value provided in the constructor.");
    }

    /**
     * Tests the creation of an AgentClock and verifies its initial state.
     */
    @Test
    void testGenerateAgentClock() {
        AgentClock agentClock = modelClock.generateAgentCLock();

        // Verify that the AgentClock has the same configuration as the ModelClock.
        assertNotNull(agentClock, "Generated AgentClock should not be null.");
        assertEquals(100, agentClock.getTotalNumOfTicksToRun(), "AgentClock total ticks should match the ModelClock configuration.");
        assertEquals(0, agentClock.getTicksSinceStart(), "AgentClock should start with zero ticks since start.");
        assertEquals(0, agentClock.getTicksSinceWarmUp(), "AgentClock should start with zero ticks since warm-up.");
        assertFalse(agentClock.isWarmedUp(), "AgentClock should not be warmed up initially.");
    }

    /**
     * Tests the advancement of the AgentClock ticks and warm-up logic.
     */
    @Test
    void testAgentClockTicking() {
        AgentClock agentClock = modelClock.generateAgentCLock();

        // Simulate ticks up to the warm-up period.
        for (int i = 0; i < 10; i++) {
            agentClock.tick();
            assertEquals(i + 1, agentClock.getTicksSinceStart(), "Ticks since start should increment correctly.");
            assertEquals(0, agentClock.getTicksSinceWarmUp(), "Ticks since warm-up should remain zero during the warm-up period.");
            assertFalse(agentClock.isWarmedUp(), "AgentClock should not be warmed up during the warm-up period.");
        }

        // Simulate ticks after the warm-up period.
        for (int i = 0; i < 5; i++) {
            agentClock.tick();
            assertEquals(11 + i, agentClock.getTicksSinceStart(), "Ticks since start should increment correctly.");
            assertEquals(i + 1, agentClock.getTicksSinceWarmUp(), "Ticks since warm-up should increment after the warm-up period.");
            assertTrue(agentClock.isWarmedUp(), "AgentClock should be warmed up after the warm-up period.");
        }
    }
}
