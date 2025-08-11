package unit.agentarium.environments;

import agentarium.ModelSettings;
import agentarium.environments.Environment;
import agentarium.environments.FunctionalEnvironmentGenerator;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link FunctionalEnvironmentGenerator}.
 *
 * <p>Verifies that the generator delegates to the user-defined function correctly.</p>
 */
public class FunctionalEnvironmentGeneratorTest {

    @Test
    void testGenerateEnvironmentReturnsExpectedObject() {
        Environment mockEnvironment = mock(Environment.class);
        ModelSettings mockSettings = mock(ModelSettings.class);

        Function<ModelSettings, Environment> generatorFunction = settings -> mockEnvironment;

        FunctionalEnvironmentGenerator generator = new FunctionalEnvironmentGenerator(generatorFunction);

        Environment result = generator.generateEnvironment(mockSettings);

        assertSame(mockEnvironment, result, "The environment returned should match the one provided by the function");
    }
}
