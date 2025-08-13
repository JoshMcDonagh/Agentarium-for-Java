package integration.modelUsageTest1;

import agentarium.Model;
import agentarium.ModelSettings;
import agentarium.agents.DefaultAgentGenerator;
import agentarium.environments.DefaultEnvironmentGenerator;
import agentarium.results.Results;
import agentarium.scheduler.InOrderScheduler;
import integration.modelUsageTest1.attributes.ModelAttributes;
import integration.modelUsageTest1.results.ModelResults;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelUsageTest1 {

    private static double asDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return 0.0; }
    }

    private static ModelSettings getModelSettings() {
        ModelSettings settings = new ModelSettings();
        settings.setNumOfAgents(10);
        settings.setNumOfCores(2);
        settings.setNumOfTicksToRun(20);
        settings.setNumOfWarmUpTicks(10);

        settings.setBaseAgentAttributeSetCollection(ModelAttributes.getAgentAttributeSetCollection());
        settings.setBaseEnvironmentAttributeSetCollection(ModelAttributes.getEnvironmentAttributeSetCollection());

        settings.setAreProcessesSynced(false);
        settings.setDoAgentStoresHoldAgentCopies(false);
        settings.setIsCacheUsed(false);

        settings.setResultsClass(ModelResults.class);
        settings.setResults(new ModelResults());

        settings.setAgentGenerator(new DefaultAgentGenerator());
        settings.setEnvironmentGenerator(new DefaultEnvironmentGenerator());
        settings.setModelScheduler(new InOrderScheduler());
        return settings;
    }

    @Test
    public void testModelUsage() throws Exception {
        ModelSettings settings = getModelSettings();

        Results results = new Model(settings).run();

        // Aggregated per-tick hunger AFTER warm-up
        List<Object> hunger = results.getAccumulatedAgentPropertyValues("food", "Hunger");

        int expectedRows = settings.getNumOfTicksToRun();
        assertEquals(expectedRows, hunger.size(), "rows after warm-up");

        boolean sawIncrease = false;
        boolean sawDecrease = false;

        for (int t = 0; t < hunger.size(); t++) {
            double val = asDouble(hunger.get(t));
            assertTrue(Double.isFinite(val), "hunger[" + t + "] must be finite");
            assertTrue(val >= 0.0, "hunger[" + t + "] must be non-negative");

            if (t > 0) {
                double prev = asDouble(hunger.get(t - 1));
                double delta = val - prev;
                if (delta > 1e-9) sawIncrease = true;
                if (delta < -1e-9) sawDecrease = true;
            }
        }

        // With your EatFood event, we expect both rises (natural accrual) and drops (eating)
        assertTrue(sawIncrease, "series should have increases (hunger accrual)");
        assertTrue(sawDecrease, "series should have decreases (EatFood triggers)");
    }
}
