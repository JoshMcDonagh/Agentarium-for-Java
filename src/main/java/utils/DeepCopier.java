package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class DeepCopier {
    private static final Gson gson = new GsonBuilder().create();

    public static <T> T deepCopy(T original, Class<T> clazz) {
        return gson.fromJson(gson.toJson(original), clazz);
    }

    public static <T> T deepCopy(T original, Type typeOfT) {
        String json = gson.toJson(original);
        return gson.fromJson(json, typeOfT);
    }

    private DeepCopier() {
        // This class should not be instantiated.
    }
}
