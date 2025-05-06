package agentarium.attributes;

import agentarium.attributes.results.AttributeSetCollectionResults;
import com.google.gson.reflect.TypeToken;
import utils.DeepCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of {@link AttributeSet} instances associated with a single model element
 * (either an agent or the environment).
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Managing named access to attribute sets</li>
 *   <li>Initialising and recording simulation results</li>
 *   <li>Executing all attribute sets each simulation tick</li>
 * </ul>
 */
public class AttributeSetCollection {

    /** The name of the owning model element (e.g., agent or environment) */
    private String modelElementName;

    /** Maps attribute set names to their list indexes for fast access */
    private final Map<String, Integer> attributeSetIndexes = new HashMap<String, Integer>();

    /** Ordered list of attribute sets in this collection */
    private final List<AttributeSet> attributeSets = new ArrayList<AttributeSet>();

    /** Results structure for recording attribute data across ticks */
    private AttributeSetCollectionResults attributeSetCollectionResults;

    /**
     * Adds a single attribute set to the collection.
     *
     * @param attributeSet the attribute set to add
     */
    public void add(AttributeSet attributeSet) {
        Integer index = attributeSets.size();
        attributeSetIndexes.put(attributeSet.getName(), index);
        attributeSets.add(attributeSet);
    }

    /**
     * Adds multiple attribute sets to the collection.
     *
     * @param attributeSets a list of attribute sets to add
     */
    public void add(List<AttributeSet> attributeSets) {
        for (AttributeSet attributeSet : attributeSets)
            add(attributeSet);
    }

    /**
     * Retrieves an attribute set by name.
     *
     * @param name the name of the attribute set
     * @return the corresponding {@link AttributeSet}
     */
    public AttributeSet get(String name) {
        int index = attributeSetIndexes.get(name);
        return attributeSets.get(index);
    }

    /**
     * Retrieves an attribute set by its index.
     *
     * @param index the index in the list
     * @return the {@link AttributeSet} at the specified position
     */
    public AttributeSet get(int index) {
        return attributeSets.get(index);
    }

    /**
     * @return the number of attribute sets in the collection
     */
    public int size() {
        return attributeSets.size();
    }

    /**
     * Sets up this collection for simulation by assigning a model element name
     * and preparing the results structure for recording.
     *
     * @param modelElementName the name of the agent or environment this collection belongs to
     */
    public void setup(String modelElementName) {
        this.modelElementName = modelElementName;
        attributeSetCollectionResults = new AttributeSetCollectionResults();
        attributeSetCollectionResults.setup(modelElementName, attributeSets);
    }

    /**
     * @return the name of the model element this attribute set collection is associated with
     */
    public String getModelElementName() {
        return modelElementName;
    }

    /**
     * @return the results object responsible for recording outputs from each attribute set
     */
    public AttributeSetCollectionResults getResults() {
        return attributeSetCollectionResults;
    }

    /**
     * Executes all attribute sets in the collection for a single simulation tick.
     * Results are recorded into the corresponding {@link AttributeSetCollectionResults}.
     */
    public void run() {
        for (AttributeSet attributeSet : attributeSets)
            attributeSet.run(getResults().getAttributeSetResults(attributeSet.getName()));
    }

    /**
     * Creates a deep copy of this entire attribute set collection, including all contained attribute sets.
     *
     * @return a new, deep-copied {@code AttributeSetCollection}
     */
    public AttributeSetCollection deepCopyDuplicate() {
        return DeepCopier.deepCopy(this, new TypeToken<AttributeSetCollection>() {}.getType());
    }
}
