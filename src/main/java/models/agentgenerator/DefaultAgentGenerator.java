package models.agentgenerator;

import agents.Agent;
import agents.attributes.AgentAttributeSet;
import agents.attributes.event.AgentEvent;
import agents.attributes.property.AgentProperty;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The `DefaultAgentGenerator` class is responsible for generating agents
 * with predefined attributes, properties, and events. It also distributes
 * agents across computational cores in a round-robin manner.
 */
public class DefaultAgentGenerator extends AgentGenerator {

    // Maps attribute names to their corresponding attribute classes.
    private final Map<String, Class<? extends AgentAttributeSet>> attributesMap;

    // Maps attribute names to lists of their associated property classes.
    private final Map<String, List<Class<? extends AgentProperty<?>>>> propertiesMap;

    // Maps attribute names to lists of their pre-event classes.
    private final Map<String, List<Class<? extends AgentEvent>>> preEventsMap;

    // Maps attribute names to lists of their post-event classes.
    private final Map<String, List<Class<? extends AgentEvent>>> postEventsMap;

    // Counter to generate unique names for agents.
    private int agentCounter = 0;

    /**
     * Constructs a `DefaultAgentGenerator` with maps for attributes, properties, and events.
     *
     * @param attributesMap  Maps attribute names to attribute classes.
     * @param propertiesMap  Maps attribute names to lists of property classes.
     * @param preEventsMap   Maps attribute names to lists of pre-event classes.
     * @param postEventsMap  Maps attribute names to lists of post-event classes.
     */
    public DefaultAgentGenerator(
            Map<String, Class<? extends AgentAttributeSet>> attributesMap,
            Map<String, List<Class<? extends AgentProperty<?>>>> propertiesMap,
            Map<String, List<Class<? extends AgentEvent>>> preEventsMap,
            Map<String, List<Class<? extends AgentEvent>>> postEventsMap) {
        this.attributesMap = attributesMap;
        this.propertiesMap = propertiesMap;
        this.preEventsMap = preEventsMap;
        this.postEventsMap = postEventsMap;
    }

    /**
     * Distributes a list of agents across computational cores in a round-robin fashion.
     *
     * @param agentsList The list of agents to distribute.
     * @return A list of lists, where each inner list contains agents for one core.
     */
    @Override
    public List<List<Agent>> getAgentsForEachCore(List<Agent> agentsList) {
        int numberOfCores = getAssociatedModel().numberOfCores();

        // Return an empty list if there are no cores.
        if (numberOfCores < 1) {
            return new ArrayList<>();
        }

        // If only one core, assign all agents to it.
        if (numberOfCores == 1) {
            List<List<Agent>> singleCoreList = new ArrayList<>();
            singleCoreList.add(agentsList);
            return singleCoreList;
        }

        // Create a list of lists for each core.
        List<List<Agent>> agentsForEachCore = new ArrayList<>();
        for (int i = 0; i < numberOfCores; i++) {
            agentsForEachCore.add(new ArrayList<>());
        }

        // Assign agents to cores in a round-robin manner.
        int core = 0;
        for (Agent agent : agentsList) {
            agentsForEachCore.get(core).add(agent);
            core = (core + 1) % numberOfCores;
        }

        return agentsForEachCore;
    }

    /**
     * Generates a single agent with attributes, properties, and events based on the defined maps.
     *
     * @return The newly generated agent.
     */
    @Override
    public Agent generateAgent() {
        List<AgentAttributeSet> attributesList = new ArrayList<>();

        for (Map.Entry<String, Class<? extends AgentAttributeSet>> entry : attributesMap.entrySet()) {
            String attributeName = entry.getKey();
            Class<? extends AgentAttributeSet> attributeClass = entry.getValue();

            try {
                // Instantiate the attribute.
                AgentAttributeSet attribute = attributeClass.getDeclaredConstructor(String.class).newInstance(attributeName);

                // Add properties to the attribute.
                List<Class<? extends AgentProperty<?>>> propertyClasses = propertiesMap.getOrDefault(attributeName, List.of());
                for (Class<? extends AgentProperty<?>> propertyClass : propertyClasses) {
                    AgentProperty<?> property = propertyClass.getDeclaredConstructor().newInstance();
                    attribute.getProperties().addProperty(property);
                }

                // Add pre-events to the attribute.
                List<Class<? extends AgentEvent>> preEventClasses = preEventsMap.getOrDefault(attributeName, List.of());
                for (Class<? extends AgentEvent> eventClass : preEventClasses) {
                    AgentEvent event = eventClass.getDeclaredConstructor().newInstance();
                    attribute.getPreEvents().addEvent(event);
                }

                // Add post-events to the attribute.
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

        // Create the agent with a name and attributes.
        Agent agent = new Agent(String.valueOf(agentCounter), attributesList);

        // Increment the agent counter for unique naming.
        agentCounter++;

        return agent;
    }
}
