package agentarium.attributes;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link FunctionalEvent}.
 *
 * <p>Verifies that run logic is executed and the trigger logic behaves as expected.</p>
 */
public class FunctionalEventTest {

    @Test
    void testRunLogicIsExecuted() {
        // Arrange: use a flag to verify the run logic is called
        AtomicBoolean hasRun = new AtomicBoolean(false);
        Runnable runLogic = () -> hasRun.set(true);

        BooleanSupplier triggerLogic = () -> false;  // doesn't matter for this test

        FunctionalEvent event = new FunctionalEvent("RunEvent", true, runLogic, triggerLogic);

        // Act
        event.run();

        // Assert
        assertTrue(hasRun.get(), "The run logic should have been executed");
    }

    @Test
    void testIsTriggeredReturnsTrue() {
        // Arrange: use a BooleanSupplier that returns true
        Runnable runLogic = mock(Runnable.class);
        BooleanSupplier triggerLogic = () -> true;

        FunctionalEvent event = new FunctionalEvent("TriggerTrueEvent", false, runLogic, triggerLogic);

        // Act & Assert
        assertTrue(event.isTriggered(), "Expected trigger logic to return true");
    }

    @Test
    void testIsTriggeredReturnsFalse() {
        // Arrange: use a BooleanSupplier that returns false
        Runnable runLogic = mock(Runnable.class);
        BooleanSupplier triggerLogic = () -> false;

        FunctionalEvent event = new FunctionalEvent("TriggerFalseEvent", false, runLogic, triggerLogic);

        // Act & Assert
        assertFalse(event.isTriggered(), "Expected trigger logic to return false");
    }

    @Test
    void testNameAndRecordingFlagsAreSet() {
        // Arrange
        String name = "TestEvent";
        boolean isRecorded = true;
        Runnable runLogic = mock(Runnable.class);
        BooleanSupplier triggerLogic = () -> false;

        FunctionalEvent event = new FunctionalEvent(name, isRecorded, runLogic, triggerLogic);

        // Act & Assert
        assertEquals(name, event.getName());
        assertTrue(event.isRecorded());
    }
}
