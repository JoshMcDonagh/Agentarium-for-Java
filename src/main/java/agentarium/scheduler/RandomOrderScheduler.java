package agentarium.scheduler;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;

import java.util.Iterator;

public class RandomOrderScheduler implements ModelScheduler {
    @Override
    public void runTick(AgentSet agentSet) {
        Iterator<Agent> randomIterator = agentSet.getRandomIterator();
        while(randomIterator.hasNext())
            randomIterator.next().run();
    }
}
