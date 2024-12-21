package models.agentgenerator;

import agents.Agent;
import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvent;
import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;
import agents.attributes.property.AgentProperty;
import models.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the `DefaultAgentGenerator` class, validating agent generation and distribution.
 */
class DefaultAgentGeneratorTest {

    private static class StubAgentProperty extends AgentProperty<Double> {

        private double value;

        public StubAgentProperty() {
            super("StubProperty", true, Double.class);
        }

        @Override
        public void set(Double value) {
            this.value = value;
        }

        @Override
        public Double get() {
            return value;
        }

        @Override
        public void run() {
            // Do nothing for now.
        }
    }

    private static class StubAgentEvent extends AgentEvent {

        public StubAgentEvent() {
            super("StubEvent", true);
        }

        @Override
        public boolean isTriggered() {
            return false; // Stub logic for testing.
        }

        @Override
        public void run() {
            // Do nothing for now.
        }
    }

    private DefaultAgentGenerator agentGenerator;

    @Mock
    private Model mockModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, List<Class<? extends AgentProperty<?>>>> mockPropertiesMap = new HashMap<>();
        Map<String, List<Class<? extends AgentEvent>>> mockPreEventsMap = new HashMap<>();
        Map<String, List<Class<? extends AgentEvent>>> mockPostEventsMap = new HashMap<>();

        // Initialise the DefaultAgentGenerator with mock maps.
        agentGenerator = new DefaultAgentGenerator(
                Map.of("MockAttribute", AgentAttributeSet.class),
                mockPropertiesMap,
                mockPreEventsMap,
                mockPostEventsMap
        );

        List<Class<? extends AgentEvent>> events = List.of(StubAgentEvent.class);

        // Set the associated model with mock behaviour.
        when(mockModel.numberOfCores()).thenReturn(2); // Example: 2 cores
        agentGenerator.setAssociatedModel(mockModel);
    }

    /**
     * Test the generation of a single agent with attributes, properties, and events.
     */
    @Test
    void testGenerateAgent() {
        // Generate an agent
        Agent agent = agentGenerator.generateAgent();

        // Verify the agent's attributes
        assertNotNull(agent);
        assertEquals("0", agent.name()); // First agent should have name "0"

        AgentAttributeSet attribute = agent.getAttributeSet("MockAttribute");
        assertNotNull(attribute);

        // Verify properties and events
        assertNotNull(attribute.getProperties());
        assertEquals(0, attribute.getProperties().getPropertiesList().size());
        assertNotNull(attribute.getPreEvents());
        assertEquals(0, attribute.getPreEvents().getEventsList().size());
        assertNotNull(attribute.getPostEvents());
        assertEquals(0, attribute.getPostEvents().getEventsList().size());
    }

    /**
     * Test distributing agents across multiple cores.
     */
    @Test
    void testGetAgentsForEachCore() {
        // Generate a list of agents.
        List<Agent> agents = agentGenerator.generateAgents(4); // Example: 4 agents.

        // Distribute agents across cores.
        List<List<Agent>> distributedAgents = agentGenerator.getAgentsForEachCore(agents);

        // Verify the distribution.
        assertEquals(2, distributedAgents.size()); // 2 cores.
        assertEquals(2, distributedAgents.get(0).size()); // Core 1 has 2 agents.
        assertEquals(2, distributedAgents.get(1).size()); // Core 2 has 2 agents.
    }

    /**
     * Test generating agents with only one core.
     */
    @Test
    void testGetAgentsForSingleCore() {
        when(mockModel.numberOfCores()).thenReturn(1);
        agentGenerator.setAssociatedModel(mockModel);

        // Generate a list of agents.
        List<Agent> agents = agentGenerator.generateAgents(3); // Example: 3 agents.

        // Distribute agents across cores.
        List<List<Agent>> distributedAgents = agentGenerator.getAgentsForEachCore(agents);

        // Verify all agents are assigned to a single core.
        assertEquals(1, distributedAgents.size());
        assertEquals(3, distributedAgents.get(0).size());
    }

    /**
     * Test handling cases where there are no cores.
     */
    @Test
    void testGetAgentsWithNoCores() {
        when(mockModel.numberOfCores()).thenReturn(0);
        agentGenerator.setAssociatedModel(mockModel);

        // Generate a list of agents.
        List<Agent> agents = agentGenerator.generateAgents(3); // Example: 3 agents.

        // Distribute agents across cores.
        List<List<Agent>> distributedAgents = agentGenerator.getAgentsForEachCore(agents);

        // Verify no cores are returned.
        assertEquals(0, distributedAgents.size());
    }

    /**
     * Test that `generateAgents` creates the correct number of agents.
     */
    @Test
    void testGenerateAgents() {
        int numOfAgents = 5;

        // Generate agents.
        List<Agent> agents = agentGenerator.generateAgents(numOfAgents);

        // Verify the number of agents created.
        assertEquals(numOfAgents, agents.size());
        for (int i = 0; i < numOfAgents; i++) {
            assertEquals(String.valueOf(i), agents.get(i).name()); // Agent names should be sequential.
        }
    }
}
