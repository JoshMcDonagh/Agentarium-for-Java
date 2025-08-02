package agentarium;

import agentarium.attributes.AttributeSetCollection;
import utils.DeepCopyable;

/**
 * Abstract base class for all model elements in the simulation.
 *
 * <p>Each model element represents either an {@link agentarium.agents.Agent}
 * or the {@link agentarium.environments.Environment} and holds a unique name,
 * a set of attributes, and a reference to its {@link ModelElementAccessor}.
 */
public abstract class ModelElement implements DeepCopyable<ModelElement> {

    /** Unique name of the model element (e.g., agent ID or environment name) */
    private final String name;

    /** Collection of named attributes associated with this element */
    private final AttributeSetCollection attributeSetCollection;

    /** Accessor to provide runtime interaction with the model's context */
    private ModelElementAccessor modelElementAccessor;

    /**
     * Constructs a model element with the given name and attribute set collection.
     *
     * @param name the unique name of the element
     * @param attributeSetCollection the set of attributes to associate with this element
     */
    public ModelElement(String name, AttributeSetCollection attributeSetCollection) {
        this.name = name;
        this.attributeSetCollection = attributeSetCollection.deepCopy();
        this.attributeSetCollection.setAssociatedModelElement(this);
    }

    /**
     * @return the name of this model element
     */
    public String getName() {
        return name;
    }

    /**
     * @return the attribute set collection for this model element
     */
    public AttributeSetCollection getAttributeSetCollection() {
        return attributeSetCollection;
    }

    /**
     * Sets the model element accessor, which provides external access
     * to model context, data, and messaging.
     *
     * <p>This method is protected, as it should only be called during model setup.
     *
     * @param modelElementAccessor the accessor to associate with this element
     */
    protected void setModelElementAccessor(ModelElementAccessor modelElementAccessor) {
        this.modelElementAccessor = modelElementAccessor;
    }

    /**
     * @return the accessor associated with this element, if initialised
     */
    public ModelElementAccessor getModelElementAccessor() {
        return modelElementAccessor;
    }

    /**
     * Initialises the attribute set for this element by calling its setup method.
     * This should be called before the simulation begins.
     */
    public void setup() {
        attributeSetCollection.setup(getName());
    }

    /**
     * Defines the logic to be executed on each simulation tick.
     * Must be implemented by all concrete subclasses (agents and environment).
     */
    public abstract void run();
}
