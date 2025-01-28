package agentarium.environments;

import agentarium.ModelElement;
import agentarium.attributes.AttributeSetCollection;

public class Environment extends ModelElement {

    public Environment(String name, AttributeSetCollection attributeSets) {
        super(name, attributeSets);
    }

    @Override
    public void run() {
        getAttributeSetCollection().run();
    }
}
