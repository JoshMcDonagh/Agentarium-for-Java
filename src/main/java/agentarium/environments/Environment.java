package agentarium.environments;

import agentarium.ModelElement;
import agentarium.attributes.AttributeSetCollection;

/**
 * Represents the simulation environment in the model.
 *
 * <p>The environment is a type of {@link ModelElement} and holds its own
 * {@link AttributeSetCollection}, which it runs each simulation tick.
 *
 * <p>Environments typically represent shared state or conditions accessible by agents.
 */
public class Environment extends ModelElement {

    /**
     * Constructs a new environment with the given name and attribute sets.
     *
     * @param name the unique name of the environment
     * @param attributeSets the attribute sets associated with the environment
     */
    public Environment(String name, AttributeSetCollection attributeSets) {
        super(name, attributeSets);
    }

    /**
     * Executes all environment attribute sets for the current tick.
     */
    @Override
    public void run() {
        getAttributeSetCollection().run();
    }

    @Override
    public Environment deepCopy() {
        return new Environment(getName(), getAttributeSetCollection().deepCopy());
    }
}
