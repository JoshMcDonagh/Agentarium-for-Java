package models.results;

import models.modelattributes.ModelAttributeSet;
import models.modelattributes.event.ModelEvent;
import models.modelattributes.event.ModelEvents;
import models.modelattributes.property.ModelProperty;
import models.modelattributes.results.ModelAttributeResults;
import models.modelattributes.results.ModelAttributeSetResults;
import models.modelattributes.property.ModelProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link FinalModelAttributeResults} class.
 *
 * This test suite validates the behaviour of the FinalModelAttributeResults class, ensuring that:
 * - Constructor initialises required properties.
 * - Methods correctly retrieve property values and event triggers.
 * - Databases are properly disconnected when requested.
 */
class FinalModelAttributeResultsTest {

    private FinalModelAttributeResults finalModelAttributeResults;

    @Mock
    private ModelAttributeSet mockAttributeSet;

    @Mock
    private ModelProperties mockProperties;

    @Mock
    private ModelEvents mockPreEvents;

    @Mock
    private ModelEvents mockPostEvents;

    @Mock
    private ModelAttributeSetResults mockAttributeSetResults;

    @Mock
    private ModelAttributeResults mockModelAttributeResults;

    /**
     * Sets up the necessary mock objects and their behaviour before each test case.
     */
    @BeforeEach
    void setUp() {
        // Initialise mocks with Mockito
        MockitoAnnotations.openMocks(this);

        // Define mock behaviour for ModelAttributeSet
        when(mockAttributeSet.name()).thenReturn("Attribute1");
        when(mockAttributeSet.getProperties()).thenReturn(mockProperties);
        when(mockAttributeSet.getPreEvents()).thenReturn(mockPreEvents);
        when(mockAttributeSet.getPostEvents()).thenReturn(mockPostEvents);

        // Define mock behaviour for ModelProperties
        when(mockProperties.getPropertiesList()).thenReturn(List.of(mock(ModelProperty.class)));

        // Define mock behaviour for pre-events and post-events
        when(mockPreEvents.getEventsList()).thenReturn(List.of(mock(ModelEvent.class)));
        when(mockPostEvents.getEventsList()).thenReturn(List.of(mock(ModelEvent.class)));

        // Define mock behaviour for ModelAttributeSetResults
        when(mockAttributeSetResults.getPropertyValues("Property1")).thenReturn(List.of(10, 20, 30));
        when(mockAttributeSetResults.getPreEventTriggers("Event1")).thenReturn(List.of(true, false, true));
        when(mockAttributeSetResults.getPostEventTriggers("Event2")).thenReturn(List.of(false, true));

        // Define mock behaviour for ModelAttributeResults
        when(mockModelAttributeResults.getAttributeResults("Attribute1")).thenReturn(mockAttributeSetResults);

        // Create an instance of FinalModelAttributeResults using the mock results
        finalModelAttributeResults = new FinalModelAttributeResults(mockModelAttributeResults);
    }

    /**
     * Verifies that the constructor correctly initialises the internal structure.
     */
    @Test
    void testConstructorInitialisesResults() {
        assertNotNull(finalModelAttributeResults.get(), "The constructor should initialise results.");
    }

    /**
     * Tests retrieval of property values for a given attribute and property name.
     */
    @Test
    void testGetModelPropertyValues() {
        List<Object> values = finalModelAttributeResults.getModelPropertyValues("Attribute1", "Property1");
        assertEquals(List.of(10, 20, 30), values, "Property values should match the mock data.");
    }

    /**
     * Tests retrieval of pre-event triggers for a given attribute and event name.
     */
    @Test
    void testGetModelPreEventTriggers() {
        List<Boolean> triggers = finalModelAttributeResults.getModelPreEventTriggers("Attribute1", "Event1");
        assertEquals(List.of(true, false, true), triggers, "Pre-event triggers should match the mock data.");
    }

    /**
     * Tests retrieval of post-event triggers for a given attribute and event name.
     */
    @Test
    void testGetModelPostEventTriggers() {
        List<Boolean> triggers = finalModelAttributeResults.getModelPostEventTriggers("Attribute1", "Event2");
        assertEquals(List.of(false, true), triggers, "Post-event triggers should match the mock data.");
    }

    /**
     * Tests that the `disconnectDatabases` method correctly disconnects all associated databases.
     */
    @Test
    void testDisconnectDatabases() {
        // Mock the behaviour of disconnectDatabases
        doNothing().when(mockModelAttributeResults).disconnectDatabases();

        // Call the disconnect method
        finalModelAttributeResults.disconnectDatabases();

        // Verify that the method was invoked exactly once
        verify(mockModelAttributeResults, times(1)).disconnectDatabases();
    }
}
