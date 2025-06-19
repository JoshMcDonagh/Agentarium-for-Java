package agentarium.multithreading.utils;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.AttributeSetCollection;
import agentarium.environments.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link WorkerCache} class.
 *
 * <p>Tests caching behaviour for agents, agent filters, and environment data.
 */
public class WorkerCacheTest {

    private WorkerCache cache;

    @BeforeEach
    public void setup() {
        cache = new WorkerCache(false);
    }

    @Test
    public void testAddAndGetAgent() {
        Agent agent = new Agent("Agent1", new AttributeSetCollection());
        cache.addAgent(agent);
        assertTrue(cache.doesAgentExist("Agent1"));
        assertSame(agent, cache.getAgent("Agent1"));
    }

    @Test
    public void testAddAndRetrieveAgentFilter() {
        Predicate<Agent> filter = a -> a.getName().startsWith("A");
        cache.addAgentFilter(filter);
        assertTrue(cache.doesAgentFilterExist(filter));
    }

    @Test
    public void testGetFilteredAgentsFromCachedSet() {
        Agent agent1 = new Agent("A", new AttributeSetCollection());
        Agent agent2 = new Agent("B", new AttributeSetCollection());

        AgentSet set = new AgentSet();
        set.add(agent1);
        set.add(agent2);

        cache.addAgents(set);

        Predicate<Agent> startsWithA = a -> a.getName().startsWith("A");
        AgentSet filtered = cache.getFilteredAgents(startsWithA);

        assertEquals(1, filtered.size());
        assertEquals("A", filtered.get(0).getName());
    }

    @Test
    public void testAddAndGetEnvironment() {
        Environment env = new Environment("Env1", new AttributeSetCollection());
        cache.addEnvironment(env);

        assertTrue(cache.doesEnvironmentExist());
        assertSame(env, cache.getEnvironment());
    }

    @Test
    public void testClearResetsCache() {
        Agent agent = new Agent("Agent1", new AttributeSetCollection());
        Environment env = new Environment("Env", new AttributeSetCollection());
        Predicate<Agent> filter = a -> true;

        cache.addAgent(agent);
        cache.addEnvironment(env);
        cache.addAgentFilter(filter);

        cache.clear();

        assertFalse(cache.doesAgentExist("Agent1"));
        assertFalse(cache.doesAgentFilterExist(filter));
        assertFalse(cache.doesEnvironmentExist());
    }
}
