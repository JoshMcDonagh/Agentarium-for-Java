package agentarium.environments;

import agentarium.ModelSettings;

/**
 * Abstract base class responsible for generating the simulation {@link Environment}.
 *
 * <p>Concrete subclasses should use the {@link ModelSettings} to construct and return
 * a new instance of {@link Environment}, complete with its attribute sets and configuration.
 *
 * <p>This abstraction allows environments to be modular and varied across different simulations.
 */
public abstract class EnvironmentGenerator {

    /**
     * Creates and returns a fully initialised {@link Environment} for the simulation.
     *
     * @param modelSettings the global model settings used to configure the environment
     * @return a new {@link Environment} instance
     */
    public abstract Environment generateEnvironment(ModelSettings modelSettings);
}
