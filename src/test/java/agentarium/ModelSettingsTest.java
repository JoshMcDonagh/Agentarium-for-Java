package agentarium;

import agentarium.agents.AgentGenerator;
import agentarium.attributes.AttributeSetCollection;
import agentarium.environments.EnvironmentGenerator;
import agentarium.results.Results;
import agentarium.scheduler.ModelScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ModelSettings} class.
 */
public class ModelSettingsTest {

    private ModelSettings settings;

    @BeforeEach
    public void setup() {
        settings = new ModelSettings();
    }

    @Test
    public void testNumOfAgentsSetterAndGetter() {
        settings.setNumOfAgents(42);
        assertEquals(42, settings.getNumOfAgents(), "Should return the number of agents set.");
    }

    @Test
    public void testNumOfCoresSetterAndGetter() {
        settings.setNumOfCores(4);
        assertEquals(4, settings.getNumOfCores(), "Should return the number of cores set.");
    }

    @Test
    public void testNumOfTicksToRunSetterAndGetter() {
        settings.setNumOfTicksToRun(100);
        assertEquals(100, settings.getNumOfTicksToRun(), "Should return the number of ticks to run.");
    }

    @Test
    public void testNumOfWarmUpTicksSetterAndGetter() {
        settings.setNumOfWarmUpTicks(10);
        assertEquals(10, settings.getNumOfWarmUpTicks(), "Should return the number of warm-up ticks.");
    }

    @Test
    public void testTotalNumOfTicksCalculation() {
        settings.setNumOfTicksToRun(20);
        settings.setNumOfWarmUpTicks(5);
        assertEquals(25, settings.getTotalNumOfTicks(), "Total ticks should include both warm-up and active ticks.");
    }

    @Test
    public void testBaseAgentAttributeSetCollectionSetterAndGetter() {
        AttributeSetCollection collection = new AttributeSetCollection();
        settings.setBaseAgentAttributeSetCollection(collection);
        assertSame(collection, settings.getBaseAgentAttributeSetCollection(), "Should return the same agent attribute set collection that was set.");
    }

    @Test
    public void testBaseEnvironmentAttributeSetCollectionSetterAndGetter() {
        AttributeSetCollection collection = new AttributeSetCollection();
        settings.setBaseEnvironmentAttributeSetCollection(collection);
        assertSame(collection, settings.getBaseEnvironmentAttributeSetCollection(), "Should return the same environment attribute set collection that was set.");
    }

    @Test
    public void testAreProcessesSyncedSetterAndGetter() {
        settings.setAreProcessesSynced(true);
        assertTrue(settings.getAreProcessesSynced(), "Should return true if processes are set to be synchronised.");
    }

    @Test
    public void testDoAgentStoresHoldAgentCopiesSetterAndGetter() {
        settings.setDoAgentStoresHoldAgentCopies(true);
        assertTrue(settings.getDoAgentStoresHoldAgentCopies(), "Should return true if agent stores are set to hold copies.");
    }

    @Test
    public void testIsCacheUsedSetterAndGetter() {
        settings.setIsCacheUsed(true);
        assertTrue(settings.getIsCacheUsed(), "Should return true if cache usage is enabled.");
    }

    @Test
    public void testResultsClassSetterAndGetter() {
        settings.setResultsClass(MockResults.class);
        assertEquals(MockResults.class, settings.getResultsClass(), "Should return the same results class that was set.");
    }

    @Test
    public void testAgentGeneratorSetterAndGetter() {
        AgentGenerator mockGenerator = new AgentGenerator() {
            @Override
            protected agentarium.agents.Agent generateAgent(ModelSettings settings) {
                return null;
            }
        };
        settings.setAgentGenerator(mockGenerator);
        assertSame(mockGenerator, settings.getAgentGenerator(), "Should return the same agent generator that was set.");
    }

    @Test
    public void testEnvironmentGeneratorSetterAndGetter() {
        EnvironmentGenerator mockGenerator = new EnvironmentGenerator() {
            @Override
            public agentarium.environments.Environment generateEnvironment(ModelSettings settings) {
                return null;
            }
        };
        settings.setEnvironmentGenerator(mockGenerator);
        assertSame(mockGenerator, settings.getEnvironmentGenerator(), "Should return the same environment generator that was set.");
    }

    @Test
    public void testModelSchedulerSetterAndGetter() {
        ModelScheduler scheduler = agentSet -> { };
        settings.setModelScheduler(scheduler);
        assertSame(scheduler, settings.getModelScheduler(), "Should return the same model scheduler that was set.");
    }

    // Dummy Results class for testing
    public static class MockResults extends Results {
        @Override
        protected List<?> accumulateAgentPropertyResults(String attributeSetName, String propertyName, List<?> accumulatedValues, List<?> valuesToBeProcessed) {
            return valuesToBeProcessed;
        }

        @Override
        protected List<?> accumulateAgentPreEventResults(String attributeSetName, String preEventName, List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
            return valuesToBeProcessed;
        }

        @Override
        protected List<?> accumulateAgentPostEventResults(String attributeSetName, String postEventName, List<?> accumulatedValues, List<Boolean> valuesToBeProcessed) {
            return valuesToBeProcessed;
        }
    }
}
