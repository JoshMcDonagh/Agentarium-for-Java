package agents.attributes.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AgentEvent} class and its concrete subclasses.
 * These tests ensure correct functionality of the class constructors,
 * fields, and methods such as {@code isTriggered()} and {@code run()}.
 */
class AgentEventTest {

    /**
     * A concrete subclass of {@link AgentEvent} for testing purposes.
     * This implementation allows testing of abstract methods {@code isTriggered()} and {@code run()}.
     */
    static class TestAgentEvent extends AgentEvent {
        // A flag to indicate whether the event has been triggered.
        private boolean triggered = false;

        /**
         * Constructor that accepts a name and a flag indicating if the event is recorded.
         *
         * @param name       the name of the event.
         * @param isRecorded whether the event is recorded.
         */
        public TestAgentEvent(String name, boolean isRecorded) {
            super(name, isRecorded);
        }

        /**
         * Constructor that auto-generates a name and accepts a flag indicating if the event is recorded.
         *
         * @param isRecorded whether the event is recorded.
         */
        public TestAgentEvent(boolean isRecorded) {
            super(isRecorded);
        }

        /**
         * Constructor that accepts a name and defaults the event to being recorded.
         *
         * @param name the name of the event.
         */
        public TestAgentEvent(String name) {
            super(name);
        }

        /**
         * Default constructor that auto-generates a name and defaults the event to being recorded.
         */
        public TestAgentEvent() {
            super();
        }

        /**
         * Determines whether the event has been triggered.
         *
         * @return {@code true} if the event is triggered, {@code false} otherwise.
         */
        @Override
        public boolean isTriggered() {
            return triggered;
        }

        /**
         * Executes the event's behaviour by setting the {@code triggered} flag to {@code true}.
         */
        @Override
        public void run() {
            triggered = true;
        }
    }

    /**
     * Tests the constructor that accepts a name and a recording status.
     */
    @Test
    void testConstructorWithNameAndIsRecorded() {
        AgentEvent event = new TestAgentEvent("Custom Event", false);

        // Assert that the name and recording status are correctly initialised.
        assertEquals("Custom Event", event.name(), "The event name should match the provided name.");
        assertFalse(event.isRecorded(), "The event should not be recorded.");
    }

    /**
     * Tests the constructor that auto-generates a name and accepts a recording status.
     */
    @Test
    void testConstructorWithIsRecorded() {
        AgentEvent event = new TestAgentEvent(false);

        // Assert that the name is auto-generated and the recording status is correctly initialised.
        assertTrue(event.name().startsWith("Agent Event"), "The event name should be auto-generated.");
        assertFalse(event.isRecorded(), "The event should not be recorded.");
    }

    /**
     * Tests the constructor that accepts a name and defaults to being recorded.
     */
    @Test
    void testConstructorWithName() {
        AgentEvent event = new TestAgentEvent("Named Event");

        // Assert that the name and default recording status are correctly initialised.
        assertEquals("Named Event", event.name(), "The event name should match the provided name.");
        assertTrue(event.isRecorded(), "The event should default to being recorded.");
    }

    /**
     * Tests the default constructor that auto-generates a name and defaults to being recorded.
     */
    @Test
    void testDefaultConstructor() {
        AgentEvent event = new TestAgentEvent();

        // Assert that the name is auto-generated and the recording status is correctly initialised.
        assertTrue(event.name().startsWith("Agent Event"), "The event name should be auto-generated.");
        assertTrue(event.isRecorded(), "The event should default to being recorded.");
    }

    /**
     * Tests the {@code isTriggered()} method.
     */
    @Test
    void testIsTriggered() {
        TestAgentEvent event = new TestAgentEvent();

        // Assert that the event is not triggered by default.
        assertFalse(event.isTriggered(), "The event should not be triggered by default.");

        // Trigger the event and verify the state.
        event.run();
        assertTrue(event.isTriggered(), "The event should be triggered after running.");
    }

    /**
     * Tests the {@code run()} method to ensure it triggers the event.
     */
    @Test
    void testRun() {
        TestAgentEvent event = new TestAgentEvent();

        // Assert the initial state and state after running the event.
        assertFalse(event.isTriggered(), "The event should not be triggered initially.");
        event.run();
        assertTrue(event.isTriggered(), "The event should be triggered after run() is called.");
    }
}
