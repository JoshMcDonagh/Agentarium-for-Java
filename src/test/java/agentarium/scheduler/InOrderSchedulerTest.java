package agentarium.scheduler;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link InOrderScheduler}.
 *
 * <p>Verifies that agents are executed in sequence using their {@code run()} method.
 */
public class InOrderSchedulerTest {

    @Test
    void testRunTickCallsRunInOrder() {
        // Arrange: create mock agents
        Agent agent1 = mock(Agent.class);
        Agent agent2 = mock(Agent.class);
        Agent agent3 = mock(Agent.class);

        when(agent1.getName()).thenReturn("agent1");
        when(agent2.getName()).thenReturn("agent2");
        when(agent3.getName()).thenReturn("agent3");

        AgentSet agentSet = new AgentSet();
        agentSet.add(agent1);
        agentSet.add(agent2);
        agentSet.add(agent3);

        InOrderScheduler scheduler = new InOrderScheduler();

        // Act: run the scheduler
        scheduler.runTick(agentSet);

        // Assert: verify run() is called on each agent in order
        InOrder inOrder = inOrder(agent1, agent2, agent3);
        inOrder.verify(agent1).run();
        inOrder.verify(agent2).run();
        inOrder.verify(agent3).run();
    }
}
