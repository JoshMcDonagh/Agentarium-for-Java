package agentarium.integration.modelUsageTest1.results;

import agentarium.results.Results;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelResults extends Results {
    @Override
    protected List<?> accumulateAgentPropertyResults(String attributeSetName, String propertyName,
                                                     List<?> accumulatedValues, List<?> valuesToBeProcessed) {
        if (Objects.equals(attributeSetName, "food")) {
            if (Objects.equals(propertyName, "Hunger")) {
                List<Double> valuesToReturn = new ArrayList<>();
                for (int i = 0; i < accumulatedValues.size(); i++)
                    valuesToReturn.add(((double)accumulatedValues.get(i) + (double)valuesToBeProcessed.get(i)));
                return valuesToReturn;
            }
        }
        return new ArrayList<>();
    }

    @Override
    protected List<?> accumulateAgentPreEventResults(String attributeSetName, String preEventName,
                                                     List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
        return new ArrayList<>();
    }

    @Override
    protected List<?> accumulateAgentPostEventResults(String attributeSetName, String postEventName,
                                                      List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
        return new ArrayList<>();
    }
}
