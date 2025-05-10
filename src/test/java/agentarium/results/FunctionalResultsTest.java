package agentarium.results;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FunctionalResults}.
 *
 * <p>Verifies that the functional delegate methods are invoked correctly for accumulating
 * agent property and event data.</p>
 */
public class FunctionalResultsTest {

    private List<?> lastPropertyLeft;
    private List<?> lastPropertyRight;
    private List<Boolean> lastPreEventLeft;
    private List<Boolean> lastPreEventRight;
    private List<Boolean> lastPostEventLeft;
    private List<Boolean> lastPostEventRight;

    private FunctionalResults results;

    @BeforeEach
    void setUp() {
        // Functional delegate for accumulating property values (generic)
        BiFunction<List<?>, List<?>, List<?>> propertyAccumulator = (a, b) -> {
            lastPropertyLeft = a;
            lastPropertyRight = b;
            List<Object> combined = new ArrayList<>();
            combined.addAll(a);
            combined.addAll(b);
            return combined;
        };

        // Functional delegate for accumulating pre-event values (Boolean)
        BiFunction<List<Boolean>, List<Boolean>, List<?>> preEventAccumulator = (a, b) -> {
            lastPreEventLeft = a;
            lastPreEventRight = b;
            List<Boolean> combined = new ArrayList<>(a);
            combined.addAll(b);
            return combined;
        };

        // Functional delegate for accumulating post-event values (Boolean)
        BiFunction<List<Boolean>, List<Boolean>, List<?>> postEventAccumulator = (a, b) -> {
            lastPostEventLeft = a;
            lastPostEventRight = b;
            List<Boolean> combined = new ArrayList<>(a);
            combined.addAll(b);
            return combined;
        };

        // Construct the FunctionalResults using the above lambdas
        results = new FunctionalResults(propertyAccumulator, preEventAccumulator, postEventAccumulator);
    }

    @Test
    void testAccumulateAgentPropertyResultsUsesProvidedFunction() {
        List<?> acc = List.of("x", "y");
        List<?> incoming = List.of("z");

        List<?> result = results.accumulateAgentPropertyResults("attr", "prop", acc, incoming);

        assertEquals(List.of("x", "y", "z"), result);
        assertSame(acc, lastPropertyLeft);
        assertSame(incoming, lastPropertyRight);
    }

    @Test
    void testAccumulateAgentPreEventResultsUsesProvidedFunction() {
        List<Boolean> acc = List.of(true);
        List<Boolean> incoming = List.of(false);

        List<?> result = results.accumulateAgentPreEventResults("attr", "event", acc, incoming);

        assertEquals(List.of(true, false), result);
        assertSame(acc, lastPreEventLeft);
        assertSame(incoming, lastPreEventRight);
    }

    @Test
    void testAccumulateAgentPostEventResultsUsesProvidedFunction() {
        List<Boolean> acc = List.of(false);
        List<Boolean> incoming = List.of(true, true);

        List<?> result = results.accumulateAgentPostEventResults("attr", "event", acc, incoming);

        assertEquals(List.of(false, true, true), result);
        assertSame(acc, lastPostEventLeft);
        assertSame(incoming, lastPostEventRight);
    }
}
