package agents;

import agents.attributes.results.AgentAttributeResults;
import agents.attributes.AgentAttributeSet;
import com.google.gson.reflect.TypeToken;
import utilities.DeepCopier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an agent in a simulation environment, managing its attributes, events, clock,
 * and interactions with other components. Each agent has a unique name, a list of attribute sets,
 * and mechanisms to record results and handle execution logic.
 */
public class Agent {
    private final String name;
    private AgentClock clock;
    private final List<AgentAttributeSet> attributeSetList;
    private final Map<String, AgentAttributeSet> attributeSetMap = new HashMap<>();
    private AgentAccessor agentAccessor;
    private AgentAttributeResults results;

    /**
     * Constructs an `Agent` with a specified name, a list of attribute sets, and optional results.
     * If results are not provided, new results will be created and initialised.
     *
     * @param name             The name of the agent.
     * @param attributeSetList The list of attribute sets associated with the agent.
     * @param results          (Optional) Pre-existing results object for recording attribute data.
     */
    public Agent(String name, List<AgentAttributeSet> attributeSetList, AgentAttributeResults results) {
        this.name = name;
        this.attributeSetList = attributeSetList;
        for (AgentAttributeSet attribute : attributeSetList) {
            attributeSetMap.put(attribute.name(), attribute);
        }

        // Initialise results if not provided
        if (results == null) {
            this.results = new AgentAttributeResults();
            this.results.setup(name, attributeSetList);
        } else {
            this.results = results;
        }
    }

    /**
     * Constructs an `Agent` with a specified name and a list of attribute sets.
     * A new results object will be created and initialised automatically.
     *
     * @param name             The name of the agent.
     * @param attributes       The list of attribute sets associated with the agent.
     */
    public Agent(String name, List<AgentAttributeSet> attributes) {
        this(name, attributes, null);
    }

    /**
     * Retrieves the name of the agent.
     *
     * @return The agent's name.
     */
    public String name() {
        return name;
    }

    /**
     * Sets the agent's clock for managing simulation time.
     *
     * @param clock The `AgentClock` object to be set.
     */
    public void setClock(AgentClock clock) {
        this.clock = clock;
    }

    /**
     * Retrieves the agent's clock.
     *
     * @return The `AgentClock` object.
     */
    public AgentClock clock() {
        return clock;
    }

    /**
     * Sets the agent accessor for managing inter-agent communication.
     *
     * @param agentAccessor The `AgentAccessor` object to be set.
     */
    public void setAgentAccessor(AgentAccessor agentAccessor) {
        this.agentAccessor = agentAccessor;
    }

    /**
     * Retrieves the agent accessor.
     *
     * @return The `AgentAccessor` object.
     */
    public AgentAccessor agentAccessor() {
        return agentAccessor;
    }

    /**
     * Retrieves a specific attribute set by its name.
     *
     * @param name The name of the attribute set.
     * @return The corresponding `AgentAttributeSet` object, or `null` if not found.
     */
    public AgentAttributeSet getAttributeSet(String name) {
        return attributeSetMap.get(name);
    }

    /**
     * Retrieves a specific attribute set by its index in the list.
     *
     * @param index The index of the attribute set.
     * @return The corresponding `AgentAttributeSet` object.
     */
    public AgentAttributeSet getAttributeSetByIndex(int index) {
        return attributeSetList.get(index);
    }

    /**
     * Retrieves the list of all attribute sets associated with the agent.
     *
     * @return A list of `AgentAttributeSet` objects.
     */
    public List<AgentAttributeSet> getAttributeSetList() {
        return attributeSetList;
    }

    /**
     * Retrieves the results object for recording and accessing attribute data.
     *
     * @return The `AgentAttributeResults` object.
     */
    public AgentAttributeResults getResults() {
        return results;
    }

    /**
     * Executes the agent's lifecycle logic:
     * 1. Records pre-events for all attribute sets.
     * 2. Executes the logic of all attribute sets.
     * 3. Records property values from attribute sets.
     * 4. Records post-events for all attribute sets.
     * 5. Ticks the agent's clock to advance time.
     */
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

    /**
     * Creates a duplicate of the agent with identical attributes but without any recorded results.
     *
     * @return A new `Agent` object.
     */
    public Agent duplicateWithoutRecords() {
        Agent newAgent = new Agent(name, DeepCopier.deepCopy(attributeSetList, new TypeToken<List<AgentAttributeSet>>() {}.getType()));
        newAgent.clock = DeepCopier.deepCopy(clock, AgentClock.class);
        newAgent.results = null;
        return newAgent;
    }
}
