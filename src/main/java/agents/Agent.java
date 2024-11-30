package agents;

import agents.attributes.AgentAttributes;

import java.util.HashMap;
import java.util.Map;

public class Agent {
    private final String name;
    private AgentClock clock;
    private final AgentAttributes[] attributesArray;
    private final Map<String, AgentAttributes> attributesMap = new HashMap<String, AgentAttributes>();

    public Agent(String name, AgentAttributes[] attributesArray) {
        this.name = name;
        this.attributesArray = attributesArray;
        for (AgentAttributes attribute : attributesArray) {
            attributesMap.put(attribute.name(), attribute);
        }
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
        return attributesArray[index];
    }

    public void run() {
        for (AgentAttributes attribute : attributesArray) {
            attribute.run();
        }
        clock.tick();
    }
}
