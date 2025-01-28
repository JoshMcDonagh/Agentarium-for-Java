package agentarium.agents;

import agentarium.ModelElement;
import agentarium.attributes.AttributeSetCollection;
import com.google.gson.reflect.TypeToken;
import utils.DeepCopier;

public class Agent extends ModelElement {
    public Agent(String name, AttributeSetCollection attributeSets) {
        super(name, attributeSets);
    }

    @Override
    public void run() {
        getAttributeSetCollection().run();
    }

    public Agent deepCopyDuplicate() {
        return new Agent(getName(), DeepCopier.deepCopy(getAttributeSetCollection(), new TypeToken<AttributeSetCollection>() {}.getType()));
    }
}
