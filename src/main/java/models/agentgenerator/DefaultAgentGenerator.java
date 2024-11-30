package models.agentgenerator;

import agents.Agent;
import agents.attributes.AgentAttributes;
import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultAgentGenerator extends AgentGenerator {
    private final Map<String, Class<? extends AgentAttributes>> attributesMap;
    private final Map<String, List<Class<? extends AgentProperty<?>>>> propertiesMap;
    private final Map<String, List<Class<? extends AgentEvent>>> preEventsMap;
    private final Map<String, List<Class<? extends AgentEvent>>> postEventsMap;
    private int agentCounter = 0;

    public DefaultAgentGenerator(
            Map<String, Class<? extends AgentAttributes>> attributesMap,
            Map<String, List<Class<? extends AgentProperty<?>>>> propertiesMap,
            Map<String, List<Class<? extends AgentEvent>>> preEventsMap,
            Map<String, List<Class<? extends AgentEvent>>> postEventsMap) {
        this.attributesMap = attributesMap;
        this.propertiesMap = propertiesMap;
        this.preEventsMap = preEventsMap;
        this.postEventsMap = postEventsMap;
    }

    @Override
    public List<List<Agent>> getAgentsForEachCore(List<Agent> agentsList) {
        int numberOfCores = getAssociatedModel().getNumberOfCores();

        if (numberOfCores < 1) {
            return new ArrayList<>();
        }
        if (numberOfCores == 1) {
            List<List<Agent>> singleCoreList = new ArrayList<>();
            singleCoreList.add(agentsList);
            return singleCoreList;
        }

        List<List<Agent>> agentsForEachCore = new ArrayList<>();
        for (int i = 0; i < numberOfCores; i++) {
            agentsForEachCore.add(new ArrayList<>());
        }

        int core = 0;
        for (Agent agent : agentsList) {
            agentsForEachCore.get(core).add(agent);
            core = (core + 1) % numberOfCores;
        }

        return agentsForEachCore;
    }

    @Override
    public Agent generateAgent() {
        List<AgentAttributes> attributesList = new ArrayList<>();

        for (Map.Entry<String, Class<? extends AgentAttributes>> entry : attributesMap.entrySet()) {
            String attributeName = entry.getKey();
            Class<? extends AgentAttributes> attributeClass = entry.getValue();

            try {
                // Instantiate the attribute
                AgentAttributes attribute = attributeClass.getDeclaredConstructor(String.class).newInstance(attributeName);

                // Add properties to the attribute
                List<Class<? extends AgentProperty<?>>> propertyClasses = propertiesMap.getOrDefault(attributeName, List.of());
                for (Class<? extends AgentProperty<?>> propertyClass : propertyClasses) {
                    AgentProperty<?> property = propertyClass.getDeclaredConstructor().newInstance();
                    attribute.getProperties().addProperty(property);
                }

                // Add pre-events to the attribute
                List<Class<? extends AgentEvent>> preEventClasses = preEventsMap.getOrDefault(attributeName, List.of());
                for (Class<? extends AgentEvent> eventClass : preEventClasses) {
                    AgentEvent event = eventClass.getDeclaredConstructor().newInstance();
                    attribute.getPreEvents().addEvent(event);
                }

                // Add post-events to the attribute
                List<Class<? extends AgentEvent>> postEventClasses = postEventsMap.getOrDefault(attributeName, List.of());
                for (Class<? extends AgentEvent> eventClass : postEventClasses) {
                    AgentEvent event = eventClass.getDeclaredConstructor().newInstance();
                    attribute.getPostEvents().addEvent(event);
                }

                attributesList.add(attribute);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException("Error instantiating attribute: " + attributeName, e);
            }
        }

        // Create the agent with a name and attributes
        Agent agent = new Agent(String.valueOf(agentCounter), attributesList);

        // Increment the agent counter
        agentCounter++;

        return agent;
    }
}
