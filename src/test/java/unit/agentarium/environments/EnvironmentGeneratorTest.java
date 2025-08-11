package unit.agentarium.environments;

import agentarium.ModelSettings;
import agentarium.attributes.AttributeSetCollection;
import agentarium.environments.Environment;
import agentarium.environments.EnvironmentGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the abstract {@link EnvironmentGenerator} class
 * via a concrete subclass used for testing purposes.
 */
public class EnvironmentGeneratorTest {

    /**
     * A minimal concrete implementation of EnvironmentGenerator for testing.
     */
    private static class TestEnvironmentGenerator extends EnvironmentGenerator {
        @Override
        public Environment generateEnvironment(ModelSettings modelSettings) {
            return new Environment("GeneratedEnvironment", modelSettings.getBaseEnvironmentAttributeSetCollection());
        }
    }

    @Test
    public void testGenerateEnvironmentReturnsExpectedEnvironment() {
        ModelSettings settings = new ModelSettings();
        AttributeSetCollection mockAttributes = mock(AttributeSetCollection.class);
        when(mockAttributes.deepCopy()).thenReturn(mockAttributes);
        settings.setBaseEnvironmentAttributeSetCollection(mockAttributes);

        EnvironmentGenerator generator = new TestEnvironmentGenerator();
        Environment environment = generator.generateEnvironment(settings);

        assertNotNull(environment, "Environment should not be null.");
        assertEquals("GeneratedEnvironment", environment.getName(), "Environment name should match.");
        assertSame(mockAttributes, environment.getAttributeSetCollection(), "Environment should use attribute set from model settings.");
    }
}
