package agents.attributes.results;

import agents.attributes.AgentAttributeSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AgentAttributeResults} class.
 */
class AgentAttributeResultsTest {

    private AgentAttributeResults agentAttributeResults;
    private List<AgentAttributeSet> mockAttributeSets;

    @BeforeEach
    void setUp() {
        agentAttributeResults = new AgentAttributeResults();

        // Create mock AgentAttributeSets
        mockAttributeSets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AgentAttributeSet attributeSet = new AgentAttributeSet("AttributeSet" + i);
            mockAttributeSets.add(attributeSet);
        }

        agentAttributeResults.setup("TestAgent", mockAttributeSets);
    }

    /**
     * Tests the setup method and ensures proper initialisation.
     */
    @Test
    void testSetup() {
        assertEquals("TestAgent", agentAttributeResults.getAgentName(), "Agent name should match the one provided during setup.");
        assertEquals(mockAttributeSets.size(), agentAttributeResults.getAttributeSetCount(), "The number of attribute sets should match the setup.");

        for (int i = 0; i < mockAttributeSets.size(); i++) {
            assertNotNull(agentAttributeResults.getAttributeResults(mockAttributeSets.get(i).name()), "Attribute results should exist for each attribute set.");
        }
    }

    /**
     * Tests retrieval of attribute results by name.
     */
    @Test
    void testGetAttributeResultsByName() {
        AgentAttributeSetResults result = agentAttributeResults.getAttributeResults("AttributeSet1");
        assertNotNull(result, "Results should not be null for a valid attribute set name.");
        assertEquals("AttributeSet1", result.getAttributeName(), "The attribute set name should match.");
    }

    /**
     * Tests retrieval of attribute results by index.
     */
    @Test
    void testGetAttributeResultsByIndex() {
        AgentAttributeSetResults result = agentAttributeResults.getAttributeResultsByIndex(1);
        assertNotNull(result, "Results should not be null for a valid index.");
        assertEquals("AttributeSet1", result.getAttributeName(), "The attribute set name should match the expected index.");

        assertThrows(IndexOutOfBoundsException.class, () -> agentAttributeResults.getAttributeResultsByIndex(5), "Should throw exception for out-of-range index.");
    }

    /**
     * Tests retrieval of the attribute set count.
     */
    @Test
    void testGetAttributeSetCount() {
        assertEquals(mockAttributeSets.size(), agentAttributeResults.getAttributeSetCount(), "The count should match the number of attribute sets provided.");
    }

    /**
     * Tests the disconnectDatabases method.
     */
    @Test
    void testDisconnectDatabases() {
        agentAttributeResults.disconnectDatabases();

        assertEquals(0, agentAttributeResults.getAttributeSetCount(), "Attribute set count should be zero after disconnecting databases.");
        assertNull(agentAttributeResults.getAttributeResults("AttributeSet0"), "Attribute results should be cleared after disconnecting databases.");
    }

    /**
     * Tests the behaviour when getting results for a non-existent attribute set.
     */
    @Test
    void testGetNonExistentAttributeResults() {
        assertNull(agentAttributeResults.getAttributeResults("NonExistent"), "Should return null for non-existent attribute set name.");
    }
}
