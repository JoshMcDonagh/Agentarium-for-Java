package agentarium.attributes.functional;

import agentarium.ModelElement;

@FunctionalInterface
public interface EventIsTriggeredFunction {
    boolean isTriggered(ModelElement associatedModelElement);
}
