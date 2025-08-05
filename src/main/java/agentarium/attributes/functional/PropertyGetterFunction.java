package agentarium.attributes.functional;

import agentarium.ModelElement;

@FunctionalInterface
public interface PropertyGetterFunction<T> {
    T get(ModelElement associatedModelElement, T propertyValue);
}
