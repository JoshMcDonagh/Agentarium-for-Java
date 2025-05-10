package agentarium.attributes;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link FunctionalProperty}.
 *
 * <p>Verifies that the get, set, and run behaviours execute as expected when provided via functional interfaces.</p>
 */
public class FunctionalPropertyTest {

    @Test
    void testGetReturnsCurrentValue() {
        // Arrange: use an atomic reference to hold the backing value
        AtomicReference<String> valueHolder = new AtomicReference<>("initial");

        Supplier<String> getter = valueHolder::get;
        Consumer<String> setter = valueHolder::set;
        Runnable runLogic = mock(Runnable.class); // Not tested here

        FunctionalProperty<String> property = new FunctionalProperty<>(
                "TestProperty", true, String.class, getter, setter, runLogic
        );

        // Act
        String result = property.get();

        // Assert
        assertEquals("initial", result);
    }

    @Test
    void testSetUpdatesValueCorrectly() {
        // Arrange: same pattern as above
        AtomicReference<Integer> valueHolder = new AtomicReference<>(0);

        Supplier<Integer> getter = valueHolder::get;
        Consumer<Integer> setter = valueHolder::set;
        Runnable runLogic = mock(Runnable.class);

        FunctionalProperty<Integer> property = new FunctionalProperty<>(
                "Counter", true, Integer.class, getter, setter, runLogic
        );

        // Act
        property.set(99);

        // Assert
        assertEquals(99, property.get());
    }

    @Test
    void testRunExecutesRunLogic() {
        // Arrange
        Runnable runLogic = mock(Runnable.class);

        FunctionalProperty<Double> property = new FunctionalProperty<>(
                "RunTest", false, Double.class, () -> 0.0, v -> {}, runLogic
        );

        // Act
        property.run();

        // Assert
        verify(runLogic, times(1)).run();
    }

    @Test
    void testMetadataIsCorrect() {
        // Arrange
        Supplier<Boolean> getter = () -> true;
        Consumer<Boolean> setter = v -> {};
        Runnable runLogic = mock(Runnable.class);

        FunctionalProperty<Boolean> property = new FunctionalProperty<>(
                "Flag", true, Boolean.class, getter, setter, runLogic
        );

        // Assert
        assertEquals("Flag", property.getName());
        assertEquals(Boolean.class, property.getType());
        assertTrue(property.isRecorded());
    }
}
