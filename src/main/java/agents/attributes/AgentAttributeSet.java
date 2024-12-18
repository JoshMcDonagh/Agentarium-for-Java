package agents.attributes;

import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;

public class AgentAttributeSet {
    private static int agentAttributeCount = 0;

    private final AgentProperties properties = new AgentProperties();
    private final AgentEvents preEvents = new AgentEvents();
    private final AgentEvents postEvents = new AgentEvents();

    private final String name;

    public AgentAttributeSet(String name) {
        this.name = name;
        agentAttributeCount++;
    }

    public AgentAttributeSet() {
        this("Agent Attribute Set " + agentAttributeCount);
    }

    public String name() {
        return name;
    }

    public AgentProperties getProperties() {
        return properties;
    }

    public AgentEvents getPreEvents() {
        return preEvents;
    }

    public AgentEvents getPostEvents() {
        return postEvents;
    }

    public void run() {
        preEvents.run();
        properties.run();
        postEvents.run();
    }
}
