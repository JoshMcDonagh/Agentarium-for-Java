package integration.modelUsageTest1;

import agentarium.Model;
import agentarium.ModelSettings;
import agentarium.agents.DefaultAgentGenerator;
import agentarium.attributes.results.databases.AttributeSetResultsDatabase;
import agentarium.attributes.results.databases.AttributeSetResultsDatabaseFactory;
import agentarium.environments.DefaultEnvironmentGenerator;
import agentarium.results.Results;
import agentarium.scheduler.InOrderScheduler;
import integration.modelUsageTest1.attributes.ModelAttributes;
import integration.modelUsageTest1.results.ModelResults;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ModelUsageTest1 {

    // ---- tiny in-memory capture DB for assertions ----
    static class TestCaptureDB extends AttributeSetResultsDatabase {
        private final Map<String, List<Object>> props = new HashMap<>();
        private final Map<String, List<Object>> pre   = new HashMap<>();
        private final Map<String, List<Object>> post  = new HashMap<>();
        // Track which columns were set via bulk API (accumulated/processed writes)
        private final Set<String> bulkProps = new HashSet<>();
        private final Set<String> bulkPre   = new HashSet<>();
        private final Set<String> bulkPost  = new HashSet<>();

        @Override public <T> void addPropertyValue(String name, T v) {
            props.computeIfAbsent(name, k -> new ArrayList<>()).add(v);
        }
        @Override public <T> void addPreEventValue(String name, T v) {
            pre.computeIfAbsent(name, k -> new ArrayList<>()).add(v);
        }
        @Override public <T> void addPostEventValue(String name, T v) {
            post.computeIfAbsent(name, k -> new ArrayList<>()).add(v);
        }

        @Override public void setPropertyColumn(String name, List<Object> vs) {
            props.put(name, new ArrayList<>(vs == null ? List.of() : vs));
            bulkProps.add(name);
        }
        @Override public void setPreEventColumn(String name, List<Object> vs) {
            pre.put(name, new ArrayList<>(vs == null ? List.of() : vs));
            bulkPre.add(name);
        }
        @Override public void setPostEventColumn(String name, List<Object> vs) {
            post.put(name, new ArrayList<>(vs == null ? List.of() : vs));
            bulkPost.add(name);
        }

        @Override public List<Object> getPropertyColumnAsList(String name) {
            return props.getOrDefault(name, List.of());
        }
        @Override public List<Object> getPreEventColumnAsList(String name) {
            return pre.getOrDefault(name, List.of());
        }
        @Override public List<Object> getPostEventColumnAsList(String name) {
            return post.getOrDefault(name, List.of());
        }

        // helpers for the test
        boolean hasBulkProperty(String name) { return bulkProps.contains(name); }
    }

    private static double asDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return 0.0; }
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
    public void testModelUsage() throws Exception {
        ModelSettings settings = getModelSettings();

        // Create a NEW capture DB for every factory call and keep them all
        List<TestCaptureDB> created = new ArrayList<>();
        AttributeSetResultsDatabaseFactory.setCustomFactory(() -> {
            TestCaptureDB db = new TestCaptureDB();
            created.add(db);
            return db;
        });

        try {
            Results results = new Model(settings).run();

            // Find the DB that received the bulk (accumulated) "Hunger" column
            TestCaptureDB target = created.stream()
                    .filter(db -> db.hasBulkProperty("Hunger"))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "No accumulated DB found for 'Hunger'. Sizes seen: " +
                                    created.stream()
                                            .map(db -> db.getPropertyColumnAsList("Hunger").size())
                                            .collect(Collectors.toList())));

            List<Object> hunger = target.getPropertyColumnAsList("Hunger");

            int expectedRows = settings.getNumOfTicksToRun();
            assertEquals(expectedRows, hunger.size(), "rows after warm-up");

            for (int i = 0; i < hunger.size(); i++) {
                assertNotNull(hunger.get(i), "hunger[" + i + "] should not be null");
            }
            for (int i = 1; i < hunger.size(); i++) {
                double prev = asDouble(hunger.get(i - 1));
                double curr = asDouble(hunger.get(i));
                assertTrue(curr >= prev, "Hunger should be non-decreasing");
            }
        } finally {
            AttributeSetResultsDatabaseFactory.clearCustomFactory();
        }
    }
}
