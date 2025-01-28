package agentarium.environments;

import agentarium.ModelSettings;

public abstract class EnvironmentGenerator {
    public abstract Environment generateEnvironment(ModelSettings modelSettings);
}
