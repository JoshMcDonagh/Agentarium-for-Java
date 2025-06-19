package agentarium.environments;

import agentarium.attributes.AttributeSetCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Environment} class.
 * Ensures correct construction and tick execution.
 */
public class EnvironmentTest {

    private AttributeSetCollection mockAttributeSets;
    private Environment environment;

    @BeforeEach
    public void setUp() {
        // Create a mock AttributeSetCollection to verify interaction
        mockAttributeSets = mock(AttributeSetCollection.class);
        when(mockAttributeSets.deepCopyDuplicate()).thenReturn(mockAttributeSets);
        environment = new Environment("TestEnvironment", mockAttributeSets);
    }

    @Test
    public void testEnvironmentNameIsSet() {
        assertEquals("TestEnvironment", environment.getName(),
                "Environment name should match the one provided in the constructor.");
    }

    @Test
    public void testAttributeSetCollectionIsReturned() {
        assertSame(mockAttributeSets, environment.getAttributeSetCollection(),
                "getAttributeSetCollection() should return the same instance passed to the constructor.");
    }

    @Test
    public void testRunDelegatesToAttributeSetCollection() {
        environment.run();
        verify(mockAttributeSets, times(1)).run();
    }
}
