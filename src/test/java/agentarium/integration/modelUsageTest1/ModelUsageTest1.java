package agentarium.integration.modelUsageTest1;

import agentarium.Model;
import agentarium.ModelSettings;
import agentarium.agents.DefaultAgentGenerator;
import agentarium.environments.DefaultEnvironmentGenerator;
import agentarium.integration.modelUsageTest1.attributes.ModelAttributes;
import agentarium.integration.modelUsageTest1.results.ModelResults;
import agentarium.results.Results;
import agentarium.scheduler.InOrderScheduler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class ModelUsageTest1 {
    @Test
    public void testModelUsage() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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

        Model model = new Model(settings);
        Results results = model.run();
    }
}
