package agents.attributes.property;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AgentProperty} class, using a concrete implementation for testing.
 */
class AgentPropertyTest {

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

        public TestAgentProperty(boolean isRecorded, Class<T> type) {
            super(isRecorded, type);
        }

        public TestAgentProperty(Class<T> type) {
            super(type);
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
            // Example operation for testing (increments value if it's a number).
            if (value instanceof Integer) {
                value = (T) Integer.valueOf(((Integer) value) + 1);
            }
        }
    }

    private TestAgentProperty<Integer> intProperty;
    private TestAgentProperty<String> stringProperty;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        intProperty = new TestAgentProperty<>("Integer Property", true, Integer.class);
        stringProperty = new TestAgentProperty<>("String Property", false, String.class);
    }

    /**
     * Tests the initialisation of an AgentProperty with a name and recording status.
     */
    @Test
    void testInitialization() {
        assertEquals("Integer Property", intProperty.name(), "The property name should match the initial value.");
        assertTrue(intProperty.isRecorded(), "The isRecorded flag should match the initial value.");
        assertEquals("String Property", stringProperty.name(), "The property name should match the initial value.");
        assertFalse(stringProperty.isRecorded(), "The isRecorded flag should match the initial value.");
    }

    /**
     * Tests setting and getting the value of the property.
     */
    @Test
    void testSetAndGetValue() {
        intProperty.set(42);
        assertEquals(42, intProperty.get(), "The retrieved value should match the set value.");

        stringProperty.set("Test Value");
        assertEquals("Test Value", stringProperty.get(), "The retrieved value should match the set value.");
    }

    /**
     * Tests the run method to ensure it performs the intended behaviour.
     */
    @Test
    void testRun() {
        intProperty.set(10);
        intProperty.run();
        assertEquals(11, intProperty.get(), "The run method should increment the value by 1 for integers.");

        stringProperty.set("Test");
        stringProperty.run(); // No operation defined for strings in the test class.
        assertEquals("Test", stringProperty.get(), "The run method should not alter non-integer values.");
    }

    /**
     * Tests retrieving the type of the property value.
     */
    @Test
    void testType() {
        assertEquals(Integer.class, intProperty.type(), "The type method should return Integer for intProperty.");
        assertEquals(String.class, stringProperty.type(), "The type method should return String for stringProperty.");
    }

    /**
     * Tests default constructor initialisation.
     */
    @Test
    void testDefaultConstructor() {
        TestAgentProperty<Boolean> defaultProperty = new TestAgentProperty<>(Boolean.class);
        assertNotNull(defaultProperty.name(), "The default property should have an auto-generated name.");
        assertTrue(defaultProperty.isRecorded(), "The default property should be recorded by default.");
        assertEquals(Boolean.class, defaultProperty.type(), "The default property type should match the provided type.");
    }

    /**
     * Tests constructor initialisation with name and type.
     */
    @Test
    void testConstructorWithNameAndType() {
        TestAgentProperty<Double> namedProperty = new TestAgentProperty<>("Custom Property", true, Double.class);
        assertEquals("Custom Property", namedProperty.name(), "The property name should match the provided value.");
        assertTrue(namedProperty.isRecorded(), "The property should be recorded.");
        assertEquals(Double.class, namedProperty.type(), "The property type should match the provided type.");
    }

    /**
     * Tests constructor initialisation with recording status and type.
     */
    @Test
    void testConstructorWithIsRecordedAndType() {
        TestAgentProperty<Long> recordedProperty = new TestAgentProperty<>(false, Long.class);
        assertNotNull(recordedProperty.name(), "The property should have an auto-generated name.");
        assertFalse(recordedProperty.isRecorded(), "The property should match the provided recording status.");
        assertEquals(Long.class, recordedProperty.type(), "The property type should match the provided type.");
    }
}
