package agents;

import agents.attributes.results.AgentAttributeResults;
import agents.attributes.AgentAttributeSet;
import com.google.gson.reflect.TypeToken;
import utilities.DeepCopier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {
    private final String name;
    private AgentClock clock;
    private final List<AgentAttributeSet> attributeSetList;
    private final Map<String, AgentAttributeSet> attributeSetMap = new HashMap<String, AgentAttributeSet>();
    private AgentAccessor agentAccessor;
    private AgentAttributeResults results;

    public Agent(String name, List<AgentAttributeSet> attributeSetList, AgentAttributeResults results) {
        this.name = name;
        this.attributeSetList = attributeSetList;
        for (AgentAttributeSet attribute : attributeSetList)
            attributeSetMap.put(attribute.name(), attribute);

        if (results == null) {
            this.results = new AgentAttributeResults();
            this.results.setup(name, attributeSetList);
        } else {
            this.results = results;
        }
    }

    public Agent(String name, List<AgentAttributeSet> attributes) {
        this(name, attributes, null);
    }

    public String name() {
        return name;
    }

    public void setClock(AgentClock clock) {
        this.clock = clock;
    }

    public AgentClock clock() {
        return clock;
    }

    public void setAgentAccessor(AgentAccessor agentAccessor) {
        this.agentAccessor = agentAccessor;
    }

    public AgentAccessor agentAccessor() {
        return agentAccessor;
    }

    public AgentAttributeSet getAttributeSet(String name) {
        return attributeSetMap.get(name);
    }

    public AgentAttributeSet getAttributeSetByIndex(int index) {
        return attributeSetList.get(index);
    }

    public List<AgentAttributeSet> getAttributeSetList() {
        return attributeSetList;
    }

    public AgentAttributeResults getResults() {
        return results;
    }

    public void run() {
        // Record pre-events
        for (AgentAttributeSet attribute : attributeSetList) {
            attribute.getPreEvents().forEach(event ->
                    results.getAttributeResults(attribute.name()).recordPreEvent(event.name(), event.isTriggered())
            );
        }

        // Run attributes
        for (AgentAttributeSet attribute : attributeSetList) {
            attribute.run();
        }

        // Record properties
        for (AgentAttributeSet attribute : attributeSetList) {
            attribute.getProperties().forEach(property -> {
                if (property.isRecorded()) {
                    // Safely handle non-numeric property values
                    try {
                        double value = ((Number) property.get()).doubleValue();
                        results.getAttributeResults(attribute.name()).recordProperty(property.name(), value);
                    } catch (ClassCastException e) {
                        System.err.println("Non-numeric property '" + property.name() + "' cannot be recorded.");
                    }
                }
            });
        }

        // Record post-events
        for (AgentAttributeSet attribute : attributeSetList) {
            attribute.getPostEvents().forEach(event ->
                    results.getAttributeResults(attribute.name()).recordPostEvent(event.name(), event.isTriggered())
            );
        }

        // Tick the clock
        clock.tick();
    }

    public Agent duplicateWithoutRecords() {
        Agent newAgent = new Agent(name, DeepCopier.deepCopy(attributeSetList, new TypeToken<List<AgentAttributeSet>>() {}.getType()));
        newAgent.clock = DeepCopier.deepCopy(clock, AgentClock.class);
        newAgent.results = null;
        return newAgent;
    }
}
