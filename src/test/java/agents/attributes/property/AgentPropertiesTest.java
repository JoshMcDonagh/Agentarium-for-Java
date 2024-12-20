package agents.attributes.property;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AgentProperties} class.
 */
class AgentPropertiesTest {

    /**
     * A concrete subclass of {@link AgentProperty} for testing purposes.
     *
     * @param <T> The type of the property value.
     */
    static class TestAgentProperty<T> extends AgentProperty<T> {
        private T value;

        public TestAgentProperty(String name, boolean isRecorded, Class<T> type) {
            super(name, isRecorded, type);
        }

        @Override
        public void set(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void run() {
            // Example operation: increment if the value is an integer.
            if (value instanceof Integer) {
                value = (T) Integer.valueOf(((Integer) value) + 1);
            }
        }
    }

    private AgentProperties agentProperties;

    @BeforeEach
    void setUp() {
        agentProperties = new AgentProperties();
    }

    /**
     * Tests adding a single property to the collection.
     */
    @Test
    void testAddProperty() {
        TestAgentProperty<Integer> property = new TestAgentProperty<>("Test Property", true, Integer.class);
        agentProperties.addProperty(property);

        assertEquals(1, agentProperties.getPropertyCount(), "The property count should be 1 after adding a property.");
        assertEquals(property, agentProperties.getProperty("Test Property"), "The property should be retrievable by name.");
        assertTrue(agentProperties.getPropertiesList().contains(property), "The property should be present in the properties list.");
    }

    /**
     * Tests adding multiple properties to the collection.
     */
    @Test
    void testAddProperties() {
        TestAgentProperty<Integer> property1 = new TestAgentProperty<>("Property 1", true, Integer.class);
        TestAgentProperty<String> property2 = new TestAgentProperty<>("Property 2", false, String.class);
        agentProperties.addProperties(new AgentProperty[]{property1, property2});

        assertEquals(2, agentProperties.getPropertyCount(), "The property count should be 2 after adding multiple properties.");
        assertEquals(property1, agentProperties.getProperty("Property 1"), "Property 1 should be retrievable by name.");
        assertEquals(property2, agentProperties.getProperty("Property 2"), "Property 2 should be retrievable by name.");
    }

    /**
     * Tests retrieving a property by its index.
     */
    @Test
    void testGetPropertyByIndex() {
        TestAgentProperty<Integer> property = new TestAgentProperty<>("Indexed Property", true, Integer.class);
        agentProperties.addProperty(property);

        assertEquals(property, agentProperties.getPropertyByIndex(0), "The property should be retrievable by index.");
    }

    /**
     * Tests retrieving the total number of properties in the collection.
     */
    @Test
    void testGetPropertyCount() {
        assertEquals(0, agentProperties.getPropertyCount(), "The property count should be 0 initially.");

        agentProperties.addProperty(new TestAgentProperty<>("Property 1", true, Integer.class));
        assertEquals(1, agentProperties.getPropertyCount(), "The property count should be 1 after adding a property.");
    }

    /**
     * Tests executing the {@code run()} method of each property in the collection.
     */
    @Test
    void testRun() {
        TestAgentProperty<Integer> intProperty = new TestAgentProperty<>("Integer Property", true, Integer.class);
        intProperty.set(10);

        TestAgentProperty<String> stringProperty = new TestAgentProperty<>("String Property", false, String.class);
        stringProperty.set("Test");

        agentProperties.addProperties(new AgentProperty[]{intProperty, stringProperty});
        agentProperties.run();

        assertEquals(11, intProperty.get(), "The run method should increment integer properties.");
        assertEquals("Test", stringProperty.get(), "The run method should not alter non-integer properties.");
    }

    /**
     * Tests retrieving the names of all properties in the collection.
     */
    @Test
    void testGetPropertyNames() {
        TestAgentProperty<Integer> property1 = new TestAgentProperty<>("Property 1", true, Integer.class);
        TestAgentProperty<String> property2 = new TestAgentProperty<>("Property 2", false, String.class);

        agentProperties.addProperties(new AgentProperty[]{property1, property2});
        List<String> propertyNames = agentProperties.getPropertyNames();

        assertTrue(propertyNames.contains("Property 1"), "The names list should contain 'Property 1'.");
        assertTrue(propertyNames.contains("Property 2"), "The names list should contain 'Property 2'.");
    }

    /**
     * Tests retrieving the value of a property by its name.
     */
    @Test
    void testGetPropertyValue() {
        TestAgentProperty<Integer> property = new TestAgentProperty<>("Value Property", true, Integer.class);
        property.set(42);

        agentProperties.addProperty(property);

        assertEquals(42, agentProperties.getPropertyValue("Value Property"), "The retrieved value should match the property's value.");
        assertNull(agentProperties.getPropertyValue("Non-Existent Property"), "The value of a non-existent property should be null.");
    }

    /**
     * Tests applying an action to each property using {@code forEach()}.
     */
    @Test
    void testForEach() {
        TestAgentProperty<Integer> property1 = new TestAgentProperty<>("Property 1", true, Integer.class);
        property1.set(5);

        TestAgentProperty<Integer> property2 = new TestAgentProperty<>("Property 2", true, Integer.class);
        property2.set(10);

        agentProperties.addProperties(new AgentProperty[]{property1, property2});

        // Apply action with appropriate casting.
        agentProperties.forEach(property -> {
            if (property instanceof TestAgentProperty<?>) {
                TestAgentProperty<Integer> intProperty = (TestAgentProperty<Integer>) property;
                intProperty.set(0); // Explicitly cast and set value to 0.
            }
        });

        assertEquals(0, property1.get(), "Property 1's value should be updated to 0.");
        assertEquals(0, property2.get(), "Property 2's value should be updated to 0.");
    }

}
