package agentarium.agents;

import agentarium.ModelSettings;

import java.util.ArrayList;
import java.util.List;

public abstract class AgentGenerator {
    public List<Agent> generateAgents(int numOfAgents) {
        List<Agent> agents = new ArrayList<>();

        for (int i = 0; i < numOfAgents; i++)
            agents.add(generateAgent());

        return agents;
    }

    public List<List<Agent>> getAgentsForEachCode(int numOfAgents, int numOfCores) {
        List<Agent> agents = generateAgents(numOfAgents);

        if (numOfCores < 1)
            return new ArrayList<>();

        if (numOfCores == 1) {
            List<List<Agent>> singleCoreList = new ArrayList<>();
            singleCoreList.add(agents);
            return singleCoreList;
        }

        List<List<Agent>> agentsForEachCore = new ArrayList<>();
        for (int i = 0; i < numOfCores; i++)
            agentsForEachCore.add(new ArrayList<>());

        int core = 0;
        for (Agent agent : agents) {
            agentsForEachCore.get(core).add(agent);
            core = (core + 1) % numOfCores;
        }

        return agentsForEachCore;
    }

    protected abstract Agent generateAgent();
}
