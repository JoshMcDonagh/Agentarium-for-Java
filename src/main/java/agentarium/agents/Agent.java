package agentarium.agents;

import agentarium.ModelElement;
import agentarium.attributes.AttributeSetCollection;

public class Agent extends ModelElement {
    public Agent(String name, AttributeSetCollection attributeSets) {
        super(name, attributeSets);
    }

    public void run() {
        getAttributeSetCollection().run();
    }
}
