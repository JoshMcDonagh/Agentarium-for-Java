package integration.modelUsageTest1;

import agentarium.Model;
import agentarium.ModelSettings;
import agentarium.agents.DefaultAgentGenerator;
import agentarium.attributes.results.databases.AttributeSetResultsDatabase;
import agentarium.environments.DefaultEnvironmentGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import integration.modelUsageTest1.attributes.ModelAttributes;
import integration.modelUsageTest1.results.ModelResults;
import agentarium.results.Results;
import agentarium.scheduler.InOrderScheduler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ModelUsageTest1 {
    private static <T> T getPrivateFieldByType(Object target, Class<T> type) {
        Class<?> c = target.getClass();
        while (c != null) {
            for (Field f : c.getDeclaredFields()) {
                if (type.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        Object v = f.get(target);
                        return type.cast(v);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot access field: " + f.getName(), e);
                    }
                }
            }
            c = c.getSuperclass();
        }
        throw new IllegalArgumentException("No field of type " + type.getName() + " found");
    }

    private static boolean isOurPackage(Class<?> cls) {
        if (cls.isPrimitive()) return false;
        Package p = cls.getPackage();
        String name = (p == null) ? "" : p.getName();
        return name.startsWith("agentarium.");
    }

    private static <T> T findByTypeDeep(Object root, Class<T> type) {
        return findByTypeDeep(root, type, new IdentityHashMap<>());
    }

    @SuppressWarnings("unchecked")
    private static <T> T findByTypeDeep(Object obj, Class<T> type, Map<Object, Boolean> seen) {
        if (obj == null) return null;
        if (seen.put(obj, Boolean.TRUE) != null) return null; // cycle guard
        if (type.isInstance(obj)) return (T) obj;

        Class<?> c = obj.getClass();
        while (c != null) {
            for (Field f : c.getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    Object v = f.get(obj);
                    if (v != null && isOurPackage(v.getClass())) {
                        T found = findByTypeDeep(v, type, seen);
                        if (found != null) return found;
                    }
                } catch (IllegalAccessException ignore) {}
            }
            c = c.getSuperclass();
        }
        return null;
    }

    private static double asDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        if (v instanceof String s) {
            try { return new ObjectMapper().readValue(s, Double.class); }
            catch (Exception ignore) {
                return Double.parseDouble(s); // fallback if plain text number
            }
        }
        throw new AssertionError("Unexpected value type: " + v.getClass());
    }

    private static ModelSettings getModelSettings() {
        ModelSettings settings = new ModelSettings();
        settings.setNumOfAgents(10);
        settings.setNumOfCores(2);
        settings.setNumOfTicksToRun(20);
        settings.setNumOfWarmUpTicks(10);

        settings.setBaseAgentAttributeSetCollection(ModelAttributes.getAgentAttributeSetCollection());
        settings.setBaseEnvironmentAttributeSetCollection(ModelAttributes.getEnvironmentAttributeSetCollection());

        settings.setAreProcessesSynced(false);
        settings.setDoAgentStoresHoldAgentCopies(false);
        settings.setIsCacheUsed(false);

        settings.setResultsClass(ModelResults.class);
        settings.setResults(new ModelResults());

        settings.setAgentGenerator(new DefaultAgentGenerator());
        settings.setEnvironmentGenerator(new DefaultEnvironmentGenerator());
        settings.setModelScheduler(new InOrderScheduler());
        return settings;
    }

    @Test
    public void testModelUsage() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ModelSettings settings = getModelSettings();

        Model model = new Model(settings);
        Results results = model.run();

        AttributeSetResultsDatabase db;
        try {
            db = getPrivateFieldByType(results, AttributeSetResultsDatabase.class);
        } catch (IllegalArgumentException notFound) {
            db = findByTypeDeep(results, AttributeSetResultsDatabase.class);
        }
        assertNotNull(db, "Could not locate AttributeSetResultsDatabase via reflection");

        List<Object> hunger = db.getPropertyColumnAsList("Hunger");

        int expectedRows = settings.getNumOfTicksToRun() - settings.getNumOfWarmUpTicks();
        assertEquals(expectedRows, hunger.size(), "rows after warm-up");

        for (int i = 0; i < hunger.size(); i++) {
            assertNotNull(hunger.get(i), "hunger[" + i + "] should not be null");
        }

        for (int i = 1; i < hunger.size(); i++) {
            double prev = asDouble(hunger.get(i - 1));
            double curr = asDouble(hunger.get(i));
            assertTrue(curr >= prev, "Hunger should be non-decreasing");
        }
    }
}
