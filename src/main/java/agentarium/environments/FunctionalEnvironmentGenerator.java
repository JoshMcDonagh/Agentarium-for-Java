package agentarium.environments;

import agentarium.ModelSettings;

import java.util.function.Function;

/**
 * A functional implementation of {@link EnvironmentGenerator} that delegates
 * environment creation to a user-provided function.
 *
 * <p>Useful when working across languages (e.g. from Python), or when
 * modular configuration is required without subclassing.</p>
 */
public class FunctionalEnvironmentGenerator extends EnvironmentGenerator {

    private final Function<ModelSettings, Environment> generatorFunction;

    /**
     * Constructs a new functional generator.
     *
     * @param generatorFunction the function used to generate the environment
     */
    public FunctionalEnvironmentGenerator(Function<ModelSettings, Environment> generatorFunction) {
        this.generatorFunction = generatorFunction;
    }

    @Override
    public Environment generateEnvironment(ModelSettings modelSettings) {
        return generatorFunction.apply(modelSettings);
    }
}
