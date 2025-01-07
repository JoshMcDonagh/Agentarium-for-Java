package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Utility class for performing deep copies of objects using JSON serialisation and deserialisation.
 * This class utilises Gson, a JSON library, to serialise an object to a JSON string
 * and then deserialise it back to create a deep copy.
 */
public class DeepCopier {

    // A single instance of Gson is used to avoid the overhead of repeatedly creating new Gson objects.
    private static final Gson gson = new GsonBuilder().create();

    /**
     * Creates a deep copy of an object using its class type.
     *
     * @param original The original object to copy.
     * @param clazz    The class type of the object being copied.
     * @param <T>      The type of the object.
     * @return A deep copy of the original object.
     */
    public static <T> T deepCopy(T original, Class<T> clazz) {
        // Serialise the object to JSON and deserialise it back to create a deep copy.
        return gson.fromJson(gson.toJson(original), clazz);
    }

    /**
     * Creates a deep copy of an object using its type.
     * This method is useful for more complex generic types, such as lists, maps, or custom generic classes.
     *
     * @param original The original object to copy.
     * @param typeOfT  The Type of the object being copied, often provided using `TypeToken` from Gson.
     * @param <T>      The type of the object.
     * @return A deep copy of the original object.
     */
    public static <T> T deepCopy(T original, Type typeOfT) {
        // Serialise the object to a JSON string.
        String json = gson.toJson(original);
        // Deserialise the JSON string back into the specified type to create a deep copy.
        return gson.fromJson(json, typeOfT);
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DeepCopier() {
        // This class should not be instantiated.
    }
}
