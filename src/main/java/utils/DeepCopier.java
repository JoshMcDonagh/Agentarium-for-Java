package utils;

import com.google.gson.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Utility class providing methods to perform deep copies of objects
 * using JSON serialisation and deserialisation via the Gson library.
 *
 * <p>This approach may not be suitable for all object types,
 * particularly those with non-serialisable fields or transient states.
 */
public final class DeepCopier {

    // Prevent instantiation
    private DeepCopier() {}

    // Gson instance configured to ignore Class<?> fields and transient/static fields
    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            // Ignore Class<?> fields even inside collections/nested objects
            .registerTypeAdapter(Class.class, new JsonSerializer<Class<?>>() {
                @Override
                public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
                    return JsonNull.INSTANCE; // Prevent crash
                }
            })
            .registerTypeAdapter(Class.class, new JsonDeserializer<Class<?>>() {
                @Override
                public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return null; // Leave as null when deserializing
                }
            })
            .create();

    /**
     * Creates a deep copy of the given object using its class type.
     *
     * @param original the object to be copied
     * @param clazz    the class of the object to be copied
     * @param <T>      the type of the object
     * @return a deep copy of the original object
     */
    public static <T> T deepCopy(T original, Class<T> clazz) {
        return gson.fromJson(gson.toJson(original), clazz);
    }

    /**
     * Creates a deep copy of the given object using a specified {@link Type}.
     * This is particularly useful for generic types, such as collections.
     *
     * @param original the object to be copied
     * @param typeOfT  the specific type information of the object
     * @param <T>      the type of the object
     * @return a deep copy of the original object
     */
    public static <T> T deepCopy(T original, Type typeOfT) {
        return gson.fromJson(gson.toJson(original), typeOfT);
    }
}
