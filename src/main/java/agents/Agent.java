package agents;

import agents.attributes.AgentAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {
    private final String name;
    private AgentClock clock;
    private final List<AgentAttributes> attributesList;
    private final Map<String, AgentAttributes> attributesMap = new HashMap<String, AgentAttributes>();
    private final AgentResults results;

    public Agent(String name, List<AgentAttributes> attributesList, AgentResults results) {
        this.name = name;
        this.attributesList = attributesList;
        for (AgentAttributes attribute : attributesList)
            attributesMap.put(attribute.name(), attribute);

        if (results == null) {
            this.results = new AgentResults();
            this.results.setup(name, attributesList);
        } else {
            this.results = results;
        }
    }

    public Agent(String name, List<AgentAttributes> attributes) {
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

    public AgentAttributes getAttributes(String name) {
        return attributesMap.get(name);
    }

    public AgentAttributes getAttributesByIndex(int index) {
        return attributesList.get(index);
    }

    public AgentResults getResults() {
        return results;
    }

    public void run() {
        // Record pre-events
        for (AgentAttributes attribute : attributesList) {
            attribute.getPreEvents().forEach(event ->
                    results.recordPreEvent(attribute, event.name(), event.isTriggered())
            );
        }

        // Run attributes
        for (AgentAttributes attribute : attributesList) {
            attribute.run();
        }

        // Record properties
        for (AgentAttributes attribute : attributesList) {
            attribute.getProperties().forEach(property -> {
                if (property.isRecorded()) {
                    // Safely handle non-numeric property values
                    try {
                        double value = ((Number) property.get()).doubleValue();
                        results.recordProperty(attribute, property.name(), value);
                    } catch (ClassCastException e) {
                        System.err.println("Non-numeric property '" + property.name() + "' cannot be recorded.");
                    }
                }
            });
        }

        // Record post-events
        for (AgentAttributes attribute : attributesList) {
            attribute.getPostEvents().forEach(event ->
                    results.recordPostEvent(attribute, event.name(), event.isTriggered())
            );
        }

        // Tick the clock
        clock.tick();
    }
}
