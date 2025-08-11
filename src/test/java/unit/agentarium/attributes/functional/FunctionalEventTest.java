package unit.agentarium.attributes.functional;

import agentarium.attributes.functional.EventIsTriggeredFunction;
import agentarium.attributes.functional.EventRunFunction;
import agentarium.attributes.functional.FunctionalEvent;
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
        EventRunFunction runLogic = (associatedModelElement) -> hasRun.set(true);

        EventIsTriggeredFunction triggerLogic = (associatedModelElement) -> false;  // doesn't matter for this test

        FunctionalEvent event = new FunctionalEvent("RunEvent", true, runLogic, triggerLogic);

        // Act
        event.run();

        // Assert
        assertTrue(hasRun.get(), "The run logic should have been executed");
    }

    @Test
    void testIsTriggeredReturnsTrue() {
        // Arrange: use a BooleanSupplier that returns true
        EventRunFunction runLogic = mock(EventRunFunction.class);
        EventIsTriggeredFunction triggerLogic = (associatedModelElement) -> true;

        FunctionalEvent event = new FunctionalEvent("TriggerTrueEvent", false, runLogic, triggerLogic);

        // Act & Assert
        assertTrue(event.isTriggered(), "Expected trigger logic to return true");
    }

    @Test
    void testIsTriggeredReturnsFalse() {
        // Arrange: use a BooleanSupplier that returns false
        EventRunFunction runLogic = mock(EventRunFunction.class);
        EventIsTriggeredFunction triggerLogic = (associatedModelElement) -> false;

        FunctionalEvent event = new FunctionalEvent("TriggerFalseEvent", false, runLogic, triggerLogic);

        // Act & Assert
        assertFalse(event.isTriggered(), "Expected trigger logic to return false");
    }

    @Test
    void testNameAndRecordingFlagsAreSet() {
        // Arrange
        String name = "TestEvent";
        boolean isRecorded = true;
        EventRunFunction runLogic = mock(EventRunFunction.class);
        EventIsTriggeredFunction triggerLogic = (associatedModelElement) -> false;

        FunctionalEvent event = new FunctionalEvent(name, isRecorded, runLogic, triggerLogic);

        // Act & Assert
        assertEquals(name, event.getName());
        assertTrue(event.isRecorded());
    }
}
