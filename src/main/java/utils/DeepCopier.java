package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Utility class providing methods to perform deep copies of objects
 * using JSON serialisation and deserialisation via the Gson library.
 *
 * <p>This approach may not be suitable for all object types,
 * particularly those with non-serialisable fields or transient states.
 */
public abstract class DeepCopier {

    // Singleton instance of Gson used for serialisation and deserialisation
    private static final Gson gson = new GsonBuilder().create();

    /**
     * Creates a deep copy of the given object using its class type.
     *
     * @param original the object to be copied
     * @param clazz the class of the object to be copied
     * @param <T> the type of the object
     * @return a deep copy of the original object
     */
    public static <T> T deepCopy(T original, Class<T> clazz) {
        // Serialise and then deserialise the object to produce a deep copy
        return gson.fromJson(gson.toJson(original), clazz);
    }

    /**
     * Creates a deep copy of the given object using a specified {@link Type}.
     * This is particularly useful for generic types, such as collections.
     *
     * @param original the object to be copied
     * @param typeOfT the specific type information of the object
     * @param <T> the type of the object
     * @return a deep copy of the original object
     */
    public static <T> T deepCopy(T original, Type typeOfT) {
        // Convert the object to JSON and then parse it back using the provided type
        String json = gson.toJson(original);
        return gson.fromJson(json, typeOfT);
    }
}
