package agentarium.attributes.functional;

import agentarium.ModelElement;

@FunctionalInterface
public interface EventRunFunction {
    void run(ModelElement associatedModelElement);
}
