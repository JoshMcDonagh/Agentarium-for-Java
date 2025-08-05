package agentarium.attributes.functional;

import agentarium.ModelElement;

@FunctionalInterface
public interface PropertyRunFunction<T> {
    T run(ModelElement associatedModelElement, T propertyValue);
}
