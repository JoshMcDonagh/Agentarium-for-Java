package agents.attributes.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AgentEvents} class, ensuring correct functionality for managing a collection of {@link AgentEvent} objects.
 */
class AgentEventsTest {

    private AgentEvents agentEvents;

    /**
     * A concrete subclass of {@link AgentEvent} for testing purposes.
     */
    static class TestAgentEvent extends AgentEvent {
        private boolean triggered = false;

        public TestAgentEvent(String name, boolean isRecorded) {
            super(name, isRecorded);
        }

        public TestAgentEvent(String name) {
            super(name);
        }

        @Override
        public boolean isTriggered() {
            return triggered;
        }

        @Override
        public void run() {
            triggered = true;
        }

        public void setTriggered(boolean triggered) {
            this.triggered = triggered;
        }
    }

    /**
     * Sets up a new instance of {@link AgentEvents} before each test.
     */
    @BeforeEach
    void setUp() {
        agentEvents = new AgentEvents();
    }

    /**
     * Tests adding a single event to the collection.
     */
    @Test
    void testAddEvent() {
        TestAgentEvent event = new TestAgentEvent("Event1");
        agentEvents.addEvent(event);

        assertEquals(1, agentEvents.getEventCount(), "The event count should be 1.");
        assertTrue(agentEvents.containsEvent("Event1"), "The event should exist in the collection.");
        assertEquals(event, agentEvents.getEvent("Event1"), "The retrieved event should match the added event.");
    }

    /**
     * Tests adding multiple events to the collection.
     */
    @Test
    void testAddEvents() {
        TestAgentEvent event1 = new TestAgentEvent("Event1");
        TestAgentEvent event2 = new TestAgentEvent("Event2");
        agentEvents.addEvents(new AgentEvent[]{event1, event2});

        assertEquals(2, agentEvents.getEventCount(), "The event count should be 2.");
        assertTrue(agentEvents.containsEvent("Event1"), "Event1 should exist in the collection.");
        assertTrue(agentEvents.containsEvent("Event2"), "Event2 should exist in the collection.");
    }

    /**
     * Tests retrieving an event by its name.
     */
    @Test
    void testGetEvent() {
        TestAgentEvent event = new TestAgentEvent("Event1");
        agentEvents.addEvent(event);

        assertNotNull(agentEvents.getEvent("Event1"), "The event should be retrievable by its name.");
        assertNull(agentEvents.getEvent("NonExistentEvent"), "Retrieving a non-existent event should return null.");
    }

    /**
     * Tests retrieving an event by its index in the ordered list.
     */
    @Test
    void testGetEventByIndex() {
        TestAgentEvent event1 = new TestAgentEvent("Event1");
        TestAgentEvent event2 = new TestAgentEvent("Event2");
        agentEvents.addEvents(new AgentEvent[]{event1, event2});

        assertEquals(event1, agentEvents.getEventByIndex(0), "The event at index 0 should be Event1.");
        assertEquals(event2, agentEvents.getEventByIndex(1), "The event at index 1 should be Event2.");
    }

    /**
     * Tests retrieving the names of all events in the collection.
     */
    @Test
    void testGetEventNames() {
        TestAgentEvent event1 = new TestAgentEvent("Event1");
        TestAgentEvent event2 = new TestAgentEvent("Event2");
        agentEvents.addEvents(new AgentEvent[]{event1, event2});

        List<String> eventNames = agentEvents.getEventNames();
        assertTrue(eventNames.contains("Event1"), "Event names should include Event1.");
        assertTrue(eventNames.contains("Event2"), "Event names should include Event2.");
    }

    /**
     * Tests running all triggered events.
     */
    @Test
    void testRun() {
        TestAgentEvent event1 = new TestAgentEvent("Event1");
        TestAgentEvent event2 = new TestAgentEvent("Event2");
        event1.setTriggered(true);
        agentEvents.addEvents(new AgentEvent[]{event1, event2});

        agentEvents.run();

        assertTrue(event1.isTriggered(), "Triggered events should run.");
        assertFalse(event2.isTriggered(), "Non-triggered events should not run.");
    }

    /**
     * Tests applying a specified action to each event in the collection.
     */
    @Test
    void testForEach() {
        TestAgentEvent event1 = new TestAgentEvent("Event1");
        TestAgentEvent event2 = new TestAgentEvent("Event2");
        agentEvents.addEvents(new AgentEvent[]{event1, event2});

        AtomicInteger count = new AtomicInteger();
        agentEvents.forEach(event -> count.incrementAndGet());

        assertEquals(2, count.get(), "The action should be applied to all events.");
    }

    /**
     * Tests checking whether a specific event is triggered.
     */
    @Test
    void testIsEventTriggered() {
        TestAgentEvent event = new TestAgentEvent("Event1");
        agentEvents.addEvent(event);

        assertFalse(agentEvents.isEventTriggered("Event1"), "The event should not be triggered by default.");
        event.setTriggered(true);
        assertTrue(agentEvents.isEventTriggered("Event1"), "The event should be triggered after setting it to triggered.");
        assertNull(agentEvents.isEventTriggered("NonExistentEvent"), "Non-existent events should return null.");
    }
}
