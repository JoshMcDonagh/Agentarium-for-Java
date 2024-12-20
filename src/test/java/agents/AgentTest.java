package agents;

import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;
import agents.attributes.results.AgentAttributeResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Agent} class.
 * Ensures the correctness of agent behaviour, including attribute management, lifecycle execution, and duplication.
 */
class AgentTest {

    private Agent agent;

    @Mock
    private AgentClock mockClock;

    @Mock
    private AgentAccessor mockAgentAccessor;

    @Mock
    private AgentAttributeSet mockAttributeSet;

    @Mock
    private AgentAttributeResults mockResults;

    /**
     * Sets up an `Agent` instance and its mocked dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock attribute set behaviour
        when(mockAttributeSet.name()).thenReturn("MockAttributeSet");

        // Create the agent with a single mocked attribute set
        agent = new Agent("TestAgent", List.of(mockAttributeSet), mockResults);
    }

    /**
     * Tests that the agent's name is correctly retrieved.
     */
    @Test
    void testName() {
        assertEquals("TestAgent", agent.name(), "Agent name should match the initial value.");
    }

    /**
     * Tests that the agent's clock can be set and retrieved.
     */
    @Test
    void testClock() {
        agent.setClock(mockClock);
        assertEquals(mockClock, agent.clock(), "Agent clock should be the same as the one set.");
    }

    /**
     * Tests that the agent accessor can be set and retrieved.
     */
    @Test
    void testAgentAccessor() {
        agent.setAgentAccessor(mockAgentAccessor);
        assertEquals(mockAgentAccessor, agent.agentAccessor(), "Agent accessor should be the same as the one set.");
    }

    /**
     * Tests that an attribute set can be retrieved by its name.
     */
    @Test
    void testGetAttributeSetByName() {
        assertEquals(mockAttributeSet, agent.getAttributeSet("MockAttributeSet"), "Attribute set should match the mock.");
    }

    /**
     * Tests that an attribute set can be retrieved by its index.
     */
    @Test
    void testGetAttributeSetByIndex() {
        assertEquals(mockAttributeSet, agent.getAttributeSetByIndex(0), "Attribute set should match the mock.");
    }

    /**
     * Tests that the list of all attribute sets is correctly retrieved.
     */
    @Test
    void testGetAttributeSetList() {
        List<AgentAttributeSet> attributeSetList = agent.getAttributeSetList();
        assertEquals(1, attributeSetList.size(), "Attribute set list should contain one item.");
        assertEquals(mockAttributeSet, attributeSetList.get(0), "Attribute set should match the mock.");
    }

    /**
     * Tests that the agent's results object is correctly retrieved.
     */
    @Test
    void testGetResults() {
        assertEquals(mockResults, agent.getResults(), "Results object should match the mock.");
    }

    /**
     * Tests the agent's lifecycle logic by verifying method calls on its components.
     */
    @Test
    void testRun() {
        // Mock pre-event and post-event behaviours
        AgentEvents mockPreEvents = mock(AgentEvents.class);
        AgentEvents mockPostEvents = mock(AgentEvents.class);
        when(mockAttributeSet.getPreEvents()).thenReturn(mockPreEvents);
        when(mockAttributeSet.getPostEvents()).thenReturn(mockPostEvents);

        // Mock properties behaviour
        AgentProperties mockProperties = mock(AgentProperties.class);
        when(mockAttributeSet.getProperties()).thenReturn(mockProperties);

        // Mock behaviour for forEach method of AgentProperties
        doAnswer(invocation -> {
            // Simulate forEach doing nothing for now (no properties to iterate)
            return null;
        }).when(mockProperties).forEach(any());

        // Set the clock to the mock
        agent.setClock(mockClock);

        // Execute the agent's lifecycle
        agent.run();

        // Verify attribute set run method and clock tick
        verify(mockAttributeSet).run();
        verify(mockClock).tick();

        // Verify properties forEach is invoked
        verify(mockProperties).forEach(any());
    }

    /**
     * Tests that a duplicate agent is created without recorded results.
     */
    @Test
    void testDuplicateWithoutRecords() {
        Agent duplicate = agent.duplicateWithoutRecords();
        assertNotNull(duplicate, "Duplicate agent should not be null.");
        assertNull(duplicate.getResults(), "Duplicate agent should not have results.");
        assertEquals(agent.name(), duplicate.name(), "Duplicate agent should have the same name.");
        assertEquals(1, duplicate.getAttributeSetList().size(), "Duplicate agent should have the same number of attribute sets.");
    }
}
