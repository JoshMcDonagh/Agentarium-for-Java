package agentarium;

import agentarium.agents.Agent;
import agentarium.agents.AgentGenerator;
import agentarium.agents.AgentSet;
import agentarium.environments.Environment;
import agentarium.environments.EnvironmentGenerator;
import agentarium.multithreading.CoordinatorThread;
import agentarium.multithreading.WorkerThread;
import agentarium.multithreading.requestresponse.RequestResponseController;
import agentarium.multithreading.requestresponse.RequestResponseInterface;
import agentarium.multithreading.utils.WorkerCache;
import agentarium.results.EnvironmentResults;
import agentarium.results.Results;
import agentarium.scheduler.ModelScheduler;
import utils.DeepCopier;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Model {
    private final ModelSettings settings;
    private final AgentGenerator agentGenerator;
    private final EnvironmentGenerator environmentGenerator;
    private ModelScheduler scheduler;

    public Model(ModelSettings settings,
                 AgentGenerator agentGenerator,
                 EnvironmentGenerator environmentGenerator,
                 ModelScheduler scheduler) {
        this.settings = settings;
        this.agentGenerator = agentGenerator;
        this.environmentGenerator = environmentGenerator;
        this.scheduler = scheduler;
    }

    public Results run() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<AgentSet> agentsForEachCore = agentGenerator.getAgentsForEachCore(settings);
        Environment environment = environmentGenerator.generateEnvironment(settings);
        Results results = settings.getResultsClass().getDeclaredConstructor().newInstance();
        results.setAgentNames(agentsForEachCore);

        environment.setup();

        ExecutorService executorService = Executors.newFixedThreadPool(settings.getNumOfCores());
        List<Future<Results>> futures = new ArrayList<>();

        RequestResponseController requestResponseController = new RequestResponseController(settings.getAreProcessesSynced());
        Thread coordinatorThread = null;

        ModelElementAccessor environmentModelElementAccessor = new ModelElementAccessor(
                environment,
                new AgentSet(),
                settings,
                new WorkerCache(settings.getDoAgentStoresHoldAgentCopies()),
                new RequestResponseInterface(environment.getName(), settings, requestResponseController),
                environment
        );
        environment.setModelElementAccessor(environmentModelElementAccessor);

        if (settings.getAreProcessesSynced()) {
            CoordinatorThread coordinator = new CoordinatorThread(
                    String.valueOf(settings.getNumOfCores()),
                    settings,
                    environment,
                    requestResponseController);
            coordinatorThread = new Thread(coordinator);
            coordinatorThread.start();
        }

        for (int coreIndex = 0; coreIndex < settings.getNumOfCores(); coreIndex++) {
            AgentSet coreAgentSet = new AgentSet(settings.getDoAgentStoresHoldAgentCopies());

            WorkerCache cache = null;
            if (settings.getIsCacheUsed())
                cache = new WorkerCache(settings.getDoAgentStoresHoldAgentCopies());

            for (Agent agent : coreAgentSet) {
                Environment localEnvironment = DeepCopier.deepCopy(environment, Environment.class);
                ModelElementAccessor agentModelElementAccessor = new ModelElementAccessor(
                        agent,
                        coreAgentSet,
                        settings,
                        cache,
                        new RequestResponseInterface(agent.getName(), settings, requestResponseController),
                        localEnvironment
                );
            }

            coreAgentSet.add(agentsForEachCore.get(coreIndex));

            Callable<Results> worker = new WorkerThread<>(
                String.valueOf(coreIndex),
                    settings,
                    scheduler,
                    requestResponseController,
                    coreAgentSet
            );

            futures.add(executorService.submit(worker));
        }

        try {
            for (Future<Results> future : futures) {
                Results coreResult = future.get();
                results.mergeWithBeforeAccumulation(coreResult);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        if (settings.getAreProcessesSynced() && coordinatorThread != null) {
            coordinatorThread.interrupt();
            try {
                coordinatorThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        results.setEnvironmentResults(new EnvironmentResults(environment));
        results.accumulateAgentAttributeData();
        results.processEnvironmentAttributeData();
        results.seal();
        return results;
    }
}
