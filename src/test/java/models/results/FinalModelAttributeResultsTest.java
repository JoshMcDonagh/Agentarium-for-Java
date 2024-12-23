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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock behavior for ModelAttributeSet
        when(mockAttributeSet.name()).thenReturn("Attribute1");
        when(mockAttributeSet.getProperties()).thenReturn(mockProperties);
        when(mockAttributeSet.getPreEvents()).thenReturn(mockPreEvents);
        when(mockAttributeSet.getPostEvents()).thenReturn(mockPostEvents);

        // Mock ModelProperties
        when(mockProperties.getPropertiesList()).thenReturn(List.of(mock(ModelProperty.class)));

        // Mock events
        when(mockPreEvents.getEventsList()).thenReturn(List.of(mock(ModelEvent.class)));
        when(mockPostEvents.getEventsList()).thenReturn(List.of(mock(ModelEvent.class)));

        // Mock attribute results
        when(mockAttributeSetResults.getPropertyValues("Property1")).thenReturn(List.of(10, 20, 30));
        when(mockAttributeSetResults.getPreEventTriggers("Event1")).thenReturn(List.of(true, false, true));
        when(mockAttributeSetResults.getPostEventTriggers("Event2")).thenReturn(List.of(false, true));

        // Mock global results behavior
        when(mockModelAttributeResults.getAttributeResults("Attribute1")).thenReturn(mockAttributeSetResults);

        // Create FinalModelAttributeResults
        finalModelAttributeResults = new FinalModelAttributeResults(List.of(mockAttributeSet));
    }


    @Test
    void testConstructorInitialisesResults() {
        assertNotNull(finalModelAttributeResults.get());
    }

    @Test
    void testGetModelPropertyValues() {
        List<Object> values = finalModelAttributeResults.getModelPropertyValues("Attribute1", "Property1");
        assertEquals(List.of(10, 20, 30), values);
    }

    @Test
    void testGetModelPreEventTriggers() {
        List<Boolean> triggers = finalModelAttributeResults.getModelPreEventTriggers("Attribute1", "Event1");
        assertEquals(List.of(true, false, true), triggers);
    }

    @Test
    void testGetModelPostEventTriggers() {
        List<Boolean> triggers = finalModelAttributeResults.getModelPostEventTriggers("Attribute1", "Event2");
        assertEquals(List.of(false, true), triggers);
    }

    @Test
    void testDisconnectDatabases() {
        // Mock the behavior
        doNothing().when(mockModelAttributeResults).disconnectDatabases();

        // Call the method
        finalModelAttributeResults.disconnectDatabases();

        // Verify invocation
        verify(mockModelAttributeResults, times(1)).disconnectDatabases();
    }
}
