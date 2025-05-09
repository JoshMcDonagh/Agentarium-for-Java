package agentarium.results;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.results.AttributeSetCollectionResults;
import agentarium.attributes.results.AttributeSetResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AgentResults}.
 */
public class AgentResultsTest {

    private Agent mockAgent1;
    private Agent mockAgent2;
    private AttributeSetCollectionResults results1;
    private AttributeSetCollectionResults results2;
    private AttributeSetResults mockAttrResults1;
    private AttributeSetResults mockAttrResults2;

    @BeforeEach
    void setUp() {
        // Mock agents and attribute results
        mockAgent1 = mock(Agent.class);
        mockAgent2 = mock(Agent.class);
        results1 = mock(AttributeSetCollectionResults.class);
        results2 = mock(AttributeSetCollectionResults.class);
        mockAttrResults1 = mock(AttributeSetResults.class);
        mockAttrResults2 = mock(AttributeSetResults.class);

        when(mockAgent1.getAttributeSetCollection().getResults()).thenReturn(results1);
        when(mockAgent2.getAttributeSetCollection().getResults()).thenReturn(results2);
        when(results1.getModelElementName()).thenReturn("A1");
        when(results2.getModelElementName()).thenReturn("A2");
        when(results1.getAttributeSetResults("behaviour")).thenReturn(mockAttrResults1);
        when(results2.getAttributeSetResults("behaviour")).thenReturn(mockAttrResults2);
        when(mockAttrResults1.getPropertyValues("prop")).thenReturn(List.of("X"));
        when(mockAttrResults2.getPostEventValues("event")).thenReturn(List.of(true));
    }

    @Test
    void testConstructionStoresAllAgents() {
        AgentSet agentSet = mock(AgentSet.class);
        when(agentSet.getAsList()).thenReturn(List.of(mockAgent1, mockAgent2));

        AgentResults agentResults = new AgentResults(agentSet);

        assertEquals(2, agentResults.getAttributeSetCollectionSetCount());
        assertNotNull(agentResults.getAttributeSetCollectionResults("A1"));
        assertNotNull(agentResults.getAttributeSetCollectionResults("A2"));
    }

    @Test
    void testGetPropertyValuesDelegatesCorrectly() {
        AgentSet agentSet = mock(AgentSet.class);
        when(agentSet.getAsList()).thenReturn(List.of(mockAgent1));
        AgentResults agentResults = new AgentResults(agentSet);

        List<Object> values = agentResults.getPropertyValues("A1", "behaviour", "prop");
        assertEquals(List.of("X"), values);
    }

    @Test
    void testGetPostEventValuesDelegatesCorrectly() {
        AgentSet agentSet = mock(AgentSet.class);
        when(agentSet.getAsList()).thenReturn(List.of(mockAgent2));
        AgentResults agentResults = new AgentResults(agentSet);

        List<Boolean> values = agentResults.getPostEventValues("A2", "behaviour", "event");
        assertEquals(List.of(true), values);
    }

    @Test
    void testDisconnectDatabasesDelegates() {
        AgentSet agentSet = mock(AgentSet.class);
        when(agentSet.getAsList()).thenReturn(List.of(mockAgent1, mockAgent2));

        AgentResults agentResults = new AgentResults(agentSet);
        agentResults.disconnectDatabases();

        verify(results1).disconnectDatabases();
        verify(results2).disconnectDatabases();
    }
}
