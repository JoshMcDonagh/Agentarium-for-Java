package agentarium.attributes.results;

import agentarium.attributes.*;
import agentarium.attributes.results.databases.AttributeSetResultsDatabase;
import agentarium.attributes.results.databases.AttributeSetResultsDatabaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the {@link AttributeSetResults} class.
 *
 * <p>Ensures correct initialisation, recording, and value retrieval
 * using mocked attributes and backing databases.
 */
public class AttributeSetResultsTest {

    private AttributeSetResults results;
    private AttributeSetResultsDatabase mockDatabase;

    @BeforeEach
    public void setup() {
        // Override the factory to return a mocked database
        mockDatabase = mock(AttributeSetResultsDatabase.class);
        AttributeSetResultsDatabaseFactory.setCustomFactory(() -> mockDatabase);

        // Build a simple attribute set with recordable items
        Properties properties = new Properties();
        properties.add(new TestProperty("prop1"));

        Events preEvents = new Events();
        preEvents.add(new TestEvent("preEvent1"));

        Events postEvents = new Events();
        postEvents.add(new TestEvent("postEvent1"));

        AttributeSet attributeSet = new AttributeSet("testSet", preEvents, properties, postEvents);
        results = new AttributeSetResults("Agent_0", attributeSet);
    }

    @Test
    public void testMetadataIsInitialisedCorrectly() {
        assertEquals("Agent_0", results.getModelElementName());
        assertEquals("testSet", results.getAttributeSetName());
        assertEquals(List.of("prop1"), results.getPropertyNamesList());
        assertEquals(List.of("preEvent1"), results.getPreEventNamesList());
        assertEquals(List.of("postEvent1"), results.getPostEventNamesList());
        assertEquals(Integer.class, results.getPropertyClass("prop1"));
    }

    @Test
    public void testRecordPropertyDelegatesToDatabase() {
        results.recordProperty("prop1", 42);
        verify(mockDatabase).addPropertyValue("prop1", 42);
    }

    @Test
    public void testRecordPreEventDelegatesToDatabase() {
        results.recordPreEvent("preEvent1", true);
        verify(mockDatabase).addPreEventValue("preEvent1", true);
    }

    @Test
    public void testRecordPostEventDelegatesToDatabase() {
        results.recordPostEvent("postEvent1", false);
        verify(mockDatabase).addPostEventValue("postEvent1", false);
    }

    @Test
    public void testDisconnectDatabaseCallsUnderlyingDisconnect() {
        results.disconnectDatabase();
        verify(mockDatabase).disconnect();
    }

    // Simple mock property for testing
    static class TestProperty extends Property<Integer> {
        public TestProperty(String name) {
            super(name, true, Integer.class);
        }
        @Override public void set(Integer value) {}
        @Override public Integer get() { return 0; }
        @Override public void run() {}
    }

    // Simple mock event for testing
    static class TestEvent extends Event {
        public TestEvent(String name) {
            super(name, true);
        }
        @Override public boolean isTriggered() { return true; }
        @Override public void run() {}
    }
}
