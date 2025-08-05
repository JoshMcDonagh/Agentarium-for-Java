package agentarium.attributes.functional;

import agentarium.ModelElement;

@FunctionalInterface
public interface PropertySetterFunction<T> {
    T set(ModelElement associatedModelElement, T currentPropertyValue, T newValue);
}
