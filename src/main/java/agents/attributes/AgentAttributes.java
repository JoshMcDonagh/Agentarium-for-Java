package agents.attributes;

import agents.attributes.event.AgentEvents;
import agents.attributes.property.AgentProperties;

public class AgentAttributes {
    private final AgentProperties properties = new AgentProperties();
    private final AgentEvents preEvents = new AgentEvents();
    private final AgentEvents postEvents = new AgentEvents();

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
