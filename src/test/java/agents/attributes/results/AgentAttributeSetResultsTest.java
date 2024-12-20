package agents.attributes.results;

import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvent;
import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;
import agents.attributes.property.AgentProperty;
import attributedatabases.AttributeResultsDatabase;
import attributedatabases.AttributeResultsDatabaseFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AgentAttributeSetResults} class.
 * These tests verify the initialisation, property/event recording,
 * data retrieval, and database disconnection functionality of the class.
 */
class AgentAttributeSetResultsTest {

    @Mock
    private AgentAttributeSet mockAttributeSet;

    @Mock
    private AttributeResultsDatabase mockDatabase;

    private AgentAttributeSetResults agentAttributeSetResults;

    private AutoCloseable closeable;

    /**
     * Sets up mocks and test environment before each test.
     * - Initialises a mock database and configures it in the factory.
     * - Creates a mock `AgentAttributeSet` and its components.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Mock the AttributeResultsDatabaseFactory to return the mock database
        mockStatic(AttributeResultsDatabaseFactory.class);
        when(AttributeResultsDatabaseFactory.createDatabase()).thenReturn(mockDatabase);

        // Mock the AgentProperties
        AgentProperties mockProperties = mock(AgentProperties.class);

        // Mock properties list
        AgentProperty<Integer> mockProperty = mock(AgentProperty.class);
        when(mockProperty.name()).thenReturn("MockProperty");
        when(mockProperty.isRecorded()).thenReturn(true);
        when(mockProperty.type()).thenReturn(Integer.class);

        // Configure the mocked AgentProperties to return the properties list
        when(mockProperties.getPropertiesList()).thenReturn(List.of(mockProperty));

        // Mock pre-event and post-event lists
        AgentEvent mockPreEvent = mock(AgentEvent.class);
        when(mockPreEvent.name()).thenReturn("MockPreEvent");
        when(mockPreEvent.isRecorded()).thenReturn(true);

        AgentEvent mockPostEvent = mock(AgentEvent.class);
        when(mockPostEvent.name()).thenReturn("MockPostEvent");
        when(mockPostEvent.isRecorded()).thenReturn(true);

        AgentEvents mockPreEvents = mock(AgentEvents.class);
        when(mockPreEvents.getEventsList()).thenReturn(List.of(mockPreEvent));

        AgentEvents mockPostEvents = mock(AgentEvents.class);
        when(mockPostEvents.getEventsList()).thenReturn(List.of(mockPostEvent));

        // Configure the mocked AgentAttributeSet
        when(mockAttributeSet.name()).thenReturn("MockAttributeSet");
        when(mockAttributeSet.getProperties()).thenReturn(mockProperties);
        when(mockAttributeSet.getPreEvents()).thenReturn(mockPreEvents);
        when(mockAttributeSet.getPostEvents()).thenReturn(mockPostEvents);

        // Create the AgentAttributeSetResults instance
        agentAttributeSetResults = new AgentAttributeSetResults("MockAgent", mockAttributeSet);
    }

    /**
     * Cleans up mocks and resets state after each test.
     */
    @AfterEach
    void tearDown() {
        // Clear the static mock to avoid conflicts in subsequent tests
        Mockito.clearAllCaches();
    }

    /**
     * Tests the initialisation of {@link AgentAttributeSetResults}.
     */
    @Test
    void testInitialisation() {
        assertEquals("MockAgent", agentAttributeSetResults.getAgentName());
        assertEquals("MockAttributeSet", agentAttributeSetResults.getAttributeName());
        assertEquals(List.of("MockProperty"), agentAttributeSetResults.getPropertyNamesList());
        assertEquals(List.of("MockPreEvent"), agentAttributeSetResults.getPreEventNamesList());
        assertEquals(List.of("MockPostEvent"), agentAttributeSetResults.getPostEventNamesList());
        assertEquals(Integer.class, agentAttributeSetResults.getPropertyClass("MockProperty"));
    }

    /**
     * Verifies that a property value is correctly recorded in the database.
     */
    @Test
    void testRecordProperty() {
        agentAttributeSetResults.recordProperty("MockProperty", 42);
        verify(mockDatabase).addPropertyValue("MockProperty", 42);
    }

    /**
     * Verifies that a pre-event value is correctly recorded in the database.
     */
    @Test
    void testRecordPreEvent() {
        agentAttributeSetResults.recordPreEvent("MockPreEvent", true);
        verify(mockDatabase).addPreEventValue("MockPreEvent", true);
    }

    /**
     * Verifies that a post-event value is correctly recorded in the database.
     */
    @Test
    void testRecordPostEvent() {
        agentAttributeSetResults.recordPostEvent("MockPostEvent", false);
        verify(mockDatabase).addPostEventValue("MockPostEvent", false);
    }

    /**
     * Ensures that property values can be retrieved from the database.
     */
    @Test
    void testGetPropertyValues() {
        List<Object> mockValues = List.of(1, 2, 3);
        when(mockDatabase.getPropertyColumnAsList("MockProperty")).thenReturn(mockValues);

        List<Object> result = agentAttributeSetResults.getPropertyValues("MockProperty");
        assertEquals(mockValues, result);
        verify(mockDatabase).getPropertyColumnAsList("MockProperty");
    }

    /**
     * Ensures that pre-event triggers can be retrieved from the database.
     */
    @Test
    void testGetPreEventTriggers() {
        List<Boolean> mockTriggers = List.of(true, false, true);
        when(mockDatabase.getPreEventColumnAsList("MockPreEvent")).thenReturn((List) mockTriggers);

        List<Boolean> result = agentAttributeSetResults.getPreEventTriggers("MockPreEvent");
        assertEquals(mockTriggers, result);
        verify(mockDatabase).getPreEventColumnAsList("MockPreEvent");
    }

    /**
     * Ensures that post-event triggers can be retrieved from the database.
     */
    @Test
    void testGetPostEventTriggers() {
        List<Boolean> mockTriggers = List.of(false, true, false);
        when(mockDatabase.getPostEventColumnAsList("MockPostEvent")).thenReturn((List) mockTriggers);

        List<Boolean> result = agentAttributeSetResults.getPostEventTriggers("MockPostEvent");
        assertEquals(mockTriggers, result);
        verify(mockDatabase).getPostEventColumnAsList("MockPostEvent");
    }

    /**
     * Verifies that the database connection is properly disconnected.
     */
    @Test
    void testDisconnectDatabase() {
        agentAttributeSetResults.disconnectDatabase();
        verify(mockDatabase).disconnect();
    }
}
