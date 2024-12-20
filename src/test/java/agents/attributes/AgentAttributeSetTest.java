package agents.attributes;

import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AgentAttributeSet} class.
 * These tests ensure proper behaviour of attribute set properties, events, and execution flow.
 */
class AgentAttributeSetTest {

    private AgentAttributeSet attributeSet;
    private AgentEvents preEvents;
    private AgentProperties properties;
    private AgentEvents postEvents;

    /**
     * Sets up the test environment with mocked dependencies.
     */
    @BeforeEach
    void setUp() {
        preEvents = mock(AgentEvents.class);
        properties = mock(AgentProperties.class);
        postEvents = mock(AgentEvents.class);

        // Inject mocks into the AgentAttributeSet
        attributeSet = new AgentAttributeSet("Test Attribute Set", properties, preEvents, postEvents);
    }

    /**
     * Tests the default constructor to ensure it assigns a default name.
     */
    @Test
    void testDefaultConstructor() {
        AgentAttributeSet defaultAttributeSet = new AgentAttributeSet();
        assertNotNull(defaultAttributeSet.name());
        assertTrue(defaultAttributeSet.name().startsWith("Agent Attribute Set"));
    }

    /**
     * Tests the custom constructor to ensure it assigns the provided name.
     */
    @Test
    void testCustomNameConstructor() {
        String customName = "Custom Attribute Set";
        AgentAttributeSet customAttributeSet = new AgentAttributeSet(customName);

        assertEquals(customName, customAttributeSet.name());
    }

    /**
     * Verifies that {@link AgentAttributeSet#getProperties()} returns a non-null instance.
     */
    @Test
    void testGetProperties() {
        assertNotNull(attributeSet.getProperties());
        assertTrue(attributeSet.getProperties() instanceof AgentProperties);
    }

    /**
     * Verifies that {@link AgentAttributeSet#getPreEvents()} returns a non-null instance.
     */
    @Test
    void testGetPreEvents() {
        assertNotNull(attributeSet.getPreEvents());
        assertTrue(attributeSet.getPreEvents() instanceof AgentEvents);
    }

    /**
     * Verifies that {@link AgentAttributeSet#getPostEvents()} returns a non-null instance.
     */
    @Test
    void testGetPostEvents() {
        assertNotNull(attributeSet.getPostEvents());
        assertTrue(attributeSet.getPostEvents() instanceof AgentEvents);
    }

    /**
     * Ensures the {@link AgentAttributeSet#run()} method executes pre-events, properties, and post-events in sequence.
     */
    @Test
    void testRunExecutesInSequence() {
        // Execute the run method
        attributeSet.run();

        // Verify the run sequence
        var inOrder = inOrder(preEvents, properties, postEvents);
        inOrder.verify(preEvents).run();
        inOrder.verify(properties).run();
        inOrder.verify(postEvents).run();
    }
}
