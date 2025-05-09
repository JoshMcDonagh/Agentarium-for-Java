package agentarium.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for managing a collection of {@link Attribute} instances.
 *
 * <p>This class provides the internal structure and core logic for attribute lookup and
 * storage. It supports both name-based and index-based access and assumes that attributes
 * are uniquely named.
 *
 * <p>Subclasses are expected to initialise and manage the {@code attributeIndexes} and
 * {@code attributes} structures, and to implement the {@link #run()} method to define
 * how the collection behaves on each simulation tick.
 */
public abstract class Attributes {

    /** Maps attribute names to their index positions in the list */
    private final Map<String, Integer> attributeIndexes = new HashMap<>();

    /** Ordered list of attribute instances */
    private final List<Attribute> attributes = new ArrayList<>();

    /**
     * Adds or replaces an attribute in the collection.
     * If an attribute with the same name already exists, it is replaced at its existing index.
     *
     * @param attribute the attribute to add or update
     */
    protected void addAttribute(Attribute attribute) {
        int index;

        if (attributeIndexes.containsKey(attribute.getName())) {
            // Replace existing attribute by name
            index = attributeIndexes.get(attribute.getName());
        } else {
            // Add new attribute at the end
            index = attributes.size();
            attributeIndexes.put(attribute.getName(), index);
            attributes.add(null); // Ensure capacity before setting
        }

        attributes.set(index, attribute);
    }

    /**
     * Retrieves an attribute by its name.
     *
     * @param attributeName the name of the attribute to retrieve
     * @return the matching {@link Attribute} instance
     */
    protected Attribute getAttribute(String attributeName) {
        int index = attributeIndexes.get(attributeName);
        return attributes.get(index);
    }

    /**
     * Retrieves an attribute by its index position in the list.
     *
     * @param index the index of the attribute
     * @return the attribute at the specified position
     */
    protected Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    /**
     * @return the number of attributes in this collection
     */
    public int size() {
        return attributes.size();
    }

    /**
     * Defines how the entire attribute collection is processed each simulation tick.
     * Subclasses must implement this method.
     */
    public abstract void run();
}
