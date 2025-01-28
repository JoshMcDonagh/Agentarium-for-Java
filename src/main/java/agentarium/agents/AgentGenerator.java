package agentarium.agents;

import agentarium.ModelSettings;
import agentarium.attributes.AttributeSetCollection;

import java.util.ArrayList;
import java.util.List;

public abstract class AgentGenerator {
    public AgentSet generateAgents(ModelSettings modelSettings) {
        AgentSet agents = new AgentSet();
        int numOfAgents = modelSettings.getNumOfAgents();

        for (int i = 0; i < numOfAgents; i++)
            agents.add(generateAgent(modelSettings));

        return agents;
    }

    public List<AgentSet> getAgentsForEachCore(ModelSettings modelSettings) {
        AgentSet agents = generateAgents(modelSettings);

        int numOfCores = modelSettings.getNumOfCores();

        if (numOfCores < 1)
            return new ArrayList<>();

        if (numOfCores == 1) {
            List<AgentSet> singleCoreList = new ArrayList<>();
            singleCoreList.add(agents);
            return singleCoreList;
        }

        List<AgentSet> agentsForEachCore = new ArrayList<>();
        for (int i = 0; i < numOfCores; i++)
            agentsForEachCore.add(new AgentSet());

        int core = 0;
        for (Agent agent : agents) {
            agentsForEachCore.get(core).add(agent);
            core = (core + 1) % numOfCores;
        }

        return agentsForEachCore;
    }

    protected abstract Agent generateAgent(ModelSettings modelSettings);
}
