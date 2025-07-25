package utils;

import agentarium.attributes.Attribute;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Utility class providing methods to perform deep copies of objects using either:
 * <ul>
 *     <li>Java Serialization (forced for Attribute trees and preferred for Serializable objects)</li>
 *     <li>Gson-based JSON serialisation (fallback for non-Attribute, non-Serializable types)</li>
 * </ul>
 *
 * <p>This approach ensures users don't need to register Attribute subclasses for Gson.
 */
public final class DeepCopier {

    // Prevent instantiation
    private DeepCopier() {}

    // Gson instance configured to ignore Class<?> fields and static/transient modifiers
    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .registerTypeAdapter(Class.class, (JsonSerializer<Class<?>>) (src, typeOfSrc, context) -> JsonNull.INSTANCE)
            .registerTypeAdapter(Class.class, (JsonDeserializer<Class<?>>) (json, typeOfT, context) -> null)
            .create();

    /**
     * Creates a deep copy of the given object.
     * <p>
     * Uses Java serialization for Attribute subclasses or Serializable objects.
     * Falls back to Gson if neither applies.
     *
     * @param original the object to be copied
     * @param clazz    the class of the object (used only for Gson fallback)
     * @param <T>      the type of the object
     * @return a deep copy of the original object
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T original, Class<T> clazz) {
        if (original instanceof Attribute || original instanceof Serializable) {
            try {
                return (T) deepCopyViaSerialization((Serializable) original);
            } catch (Exception e) {
                if (original instanceof Attribute) {
                    throw new IllegalStateException("Attribute must be serializable: " + original.getClass().getName(), e);
                }
                // fall through to Gson if not Attribute
            }
        }
        return deepCopyViaGson(original, clazz);
    }

    /**
     * Creates a deep copy of the given object using a specified {@link Type}.
     * Always uses Gson (no Attribute handling).
     *
     * @param original the object to be copied
     * @param typeOfT  the specific type information of the object
     * @param <T>      the type of the object
     * @return a deep copy of the original object
     */
    public static <T> T deepCopy(T original, Type typeOfT) {
        return gson.fromJson(gson.toJson(original), typeOfT);
    }

    /**
     * Performs deep copy via Java built-in serialization.
     *
     * @param object the Serializable object
     * @param <T>    the type of the object
     * @return deep copy
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if deserialization fails
     */
    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T deepCopyViaSerialization(T object)
            throws IOException, ClassNotFoundException {
        try (
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut)
        ) {
            out.writeObject(object);
            try (
                    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                    ObjectInputStream in = new ObjectInputStream(byteIn)
            ) {
                return (T) in.readObject();
            }
        }
    }

    /**
     * Performs deep copy using Gson serialization/deserialization.
     *
     * @param original the object
     * @param clazz    the class of the object
     * @param <T>      the type
     * @return a deep copy
     */
    private static <T> T deepCopyViaGson(T original, Class<T> clazz) {
        return gson.fromJson(gson.toJson(original), clazz);
    }
}
