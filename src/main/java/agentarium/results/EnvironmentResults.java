package agentarium.results;

import agentarium.ModelElement;
import agentarium.attributes.results.AttributeSetCollectionResults;
import agentarium.environments.Environment;

import java.util.List;

public class EnvironmentResults extends ModelElementResults {
    private final String environmentName;

    public EnvironmentResults(Environment environment) {
        super(environment);
        environmentName = environment.getName();
    }

    public List<Object> getPropertyValues(String attributeSetName, String propertyName) {
        return getPropertyValues(environmentName, attributeSetName, propertyName);
    }

    public List<Boolean> getPreEventValues(String attributeSetName, String eventName) {
        return getPreEventValues(environmentName, attributeSetName, eventName);
    }

    public List<Boolean> getPostEventValues(String attributeSetName, String eventName) {
        return getPostEventValues(environmentName, attributeSetName, eventName);
    }

    public AttributeSetCollectionResults getAttributeSetCollectionResults() {
        return getAttributeSetCollectionResults(environmentName);
    }
}
