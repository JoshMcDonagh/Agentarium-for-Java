package agentarium.environments;

import agentarium.ModelSettings;
import agentarium.attributes.AttributeSetCollection;

public class DefaultEnvironmentGenerator extends EnvironmentGenerator {
    @Override
    public Environment generateEnvironment(ModelSettings modelSettings) {
        AttributeSetCollection environmentAttributeSetCollection = modelSettings.getBaseEnvironmentAttributeSetCollection().deepCopyDuplicate();
        return new Environment("Environment", environmentAttributeSetCollection);
    }
}
