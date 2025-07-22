package agentarium.results;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Function4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FunctionalResults}.
 *
 * <p>Verifies that the functional delegate methods are invoked correctly for accumulating
 * agent property and event data.</p>
 */
public class FunctionalResultsTest {

    private String lastPropertyAttrSet;
    private String lastPropertyName;
    private List<?> lastPropertyLeft;
    private List<?> lastPropertyRight;

    private String lastPreEventAttrSet;
    private String lastPreEventName;
    private List<Boolean> lastPreEventLeft;
    private List<Boolean> lastPreEventRight;

    private String lastPostEventAttrSet;
    private String lastPostEventName;
    private List<Boolean> lastPostEventLeft;
    private List<Boolean> lastPostEventRight;

    private FunctionalResults results;

    @BeforeEach
    void setUp() {
        Function4<String, String, List<?>, List<?>, List<?>> propertyAccumulator =
                (attrSet, name, a, b) -> {
                    lastPropertyAttrSet = attrSet;
                    lastPropertyName = name;
                    lastPropertyLeft = a;
                    lastPropertyRight = b;
                    List<Object> combined = new ArrayList<>();
                    combined.addAll(a);
                    combined.addAll(b);
                    return combined;
                };

        Function4<String, String, List<Boolean>, List<Boolean>, List<?>> preEventAccumulator =
                (attrSet, name, a, b) -> {
                    lastPreEventAttrSet = attrSet;
                    lastPreEventName = name;
                    lastPreEventLeft = a;
                    lastPreEventRight = b;
                    List<Boolean> combined = new ArrayList<>(a);
                    combined.addAll(b);
                    return combined;
                };

        Function4<String, String, List<Boolean>, List<Boolean>, List<?>> postEventAccumulator =
                (attrSet, name, a, b) -> {
                    lastPostEventAttrSet = attrSet;
                    lastPostEventName = name;
                    lastPostEventLeft = a;
                    lastPostEventRight = b;
                    List<Boolean> combined = new ArrayList<>(a);
                    combined.addAll(b);
                    return combined;
                };

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
        assertEquals("attr", lastPropertyAttrSet);
        assertEquals("prop", lastPropertyName);
    }

    @Test
    void testAccumulateAgentPreEventResultsUsesProvidedFunction() {
        List<Boolean> acc = List.of(true);
        List<Boolean> incoming = List.of(false);

        List<?> result = results.accumulateAgentPreEventResults("attr", "event", acc, incoming);

        assertEquals(List.of(true, false), result);
        assertSame(acc, lastPreEventLeft);
        assertSame(incoming, lastPreEventRight);
        assertEquals("attr", lastPreEventAttrSet);
        assertEquals("event", lastPreEventName);
    }

    @Test
    void testAccumulateAgentPostEventResultsUsesProvidedFunction() {
        List<Boolean> acc = List.of(false);
        List<Boolean> incoming = List.of(true, true);

        List<?> result = results.accumulateAgentPostEventResults("attr", "event", acc, incoming);

        assertEquals(List.of(false, true, true), result);
        assertSame(acc, lastPostEventLeft);
        assertSame(incoming, lastPostEventRight);
        assertEquals("attr", lastPostEventAttrSet);
        assertEquals("event", lastPostEventName);
    }
}
