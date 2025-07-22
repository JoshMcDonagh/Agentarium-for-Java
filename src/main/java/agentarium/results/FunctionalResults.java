package agentarium.results;

import java.util.List;

import utils.Function4;

/**
 * A functional implementation of {@link Results} that allows users to define
 * accumulation logic using functional interfaces.
 *
 * <p>Useful in scenarios where subclassing is inconvenient or where logic should
 * be defined externally (e.g., via Python).</p>
 */
public class FunctionalResults extends Results {

    private final Function4<String, String, List<?>, List<?>, List<?>> accumulateProperty;
    private final Function4<String, String, List<Boolean>, List<Boolean>, List<?>> accumulatePreEvent;
    private final Function4<String, String, List<Boolean>, List<Boolean>, List<?>> accumulatePostEvent;

    /**
     * Constructs a new FunctionalResults instance.
     *
     * @param accumulateProperty  logic for accumulating agent property values
     * @param accumulatePreEvent  logic for accumulating agent pre-event values
     * @param accumulatePostEvent logic for accumulating agent post-event values
     */
    public FunctionalResults(
            Function4<String, String, List<?>, List<?>, List<?>> accumulateProperty,
            Function4<String, String, List<Boolean>, List<Boolean>, List<?>> accumulatePreEvent,
            Function4<String, String, List<Boolean>, List<Boolean>, List<?>> accumulatePostEvent
    ) {
        this.accumulateProperty = accumulateProperty;
        this.accumulatePreEvent = accumulatePreEvent;
        this.accumulatePostEvent = accumulatePostEvent;
    }

    @Override
    protected List<?> accumulateAgentPropertyResults(String attributeSetName, String propertyName,
                                                     List<?> accumulatedValues, List<?> valuesToBeProcessed) {
        return accumulateProperty.apply(attributeSetName, propertyName, accumulatedValues, valuesToBeProcessed);
    }

    @Override
    protected List<?> accumulateAgentPreEventResults(String attributeSetName, String preEventName,
                                                     List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
        return accumulatePreEvent.apply(attributeSetName, preEventName, (List<Boolean>) accumulatedValues, valuesToBeProcessed);
    }

    @Override
    protected List<?> accumulateAgentPostEventResults(String attributeSetName, String postEventName,
                                                      List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
        return accumulatePostEvent.apply(attributeSetName, postEventName, (List<Boolean>) accumulatedValues, valuesToBeProcessed);
    }
}
