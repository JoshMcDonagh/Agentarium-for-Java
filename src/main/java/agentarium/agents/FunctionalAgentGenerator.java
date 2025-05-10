package agentarium.agents;

import agentarium.ModelSettings;

import java.util.function.Function;

/**
 * An implementation of {@link AgentGenerator} that delegates agent creation logic
 * to a user-defined functional interface.
 *
 * <p>Intended for flexibility and cross-language use (e.g. from Python).</p>
 */
public class FunctionalAgentGenerator extends AgentGenerator {

    private final Function<ModelSettings, Agent> generatorFunction;

    /**
     * Constructs a new generator with the specified logic.
     *
     * @param generatorFunction the function used to generate each agent
     */
    public FunctionalAgentGenerator(Function<ModelSettings, Agent> generatorFunction) {
        this.generatorFunction = generatorFunction;
    }

    @Override
    protected Agent generateAgent(ModelSettings modelSettings) {
        return generatorFunction.apply(modelSettings);
    }
}
