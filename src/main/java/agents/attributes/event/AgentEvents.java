package agents.attributes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A container for managing a collection of {@link AgentEvent} objects.
 * This class provides methods for adding, retrieving, running, and interacting with agent events.
 */
public class AgentEvents {

    // A map for quick lookup of events by their names.
    private final Map<String, AgentEvent> eventsMap = new HashMap<>();

    // A list for ordered access to events.
    private final List<AgentEvent> eventsList = new ArrayList<>();

    /**
     * Adds a single event to the collection.
     *
     * @param newEvent The event to be added.
     */
    public void addEvent(AgentEvent newEvent) {
        eventsMap.put(newEvent.name(), newEvent); // Store the event in the map for quick lookup.
        eventsList.add(newEvent); // Maintain the order of events in the list.
    }

    /**
     * Adds multiple events to the collection.
     *
     * @param newEvents An array of events to be added.
     */
    public void addEvents(AgentEvent[] newEvents) {
        for (AgentEvent newEvent : newEvents)
            addEvent(newEvent);
    }

    /**
     * Adds multiple events to the collection.
     *
     * @param newEvents A list of events to be added.
     */
    public void addEvents(List<AgentEvent> newEvents) {
        for (AgentEvent newEvent : newEvents)
            addEvent(newEvent);
    }

    /**
     * Retrieves an event by its name.
     *
     * @param name The name of the event.
     * @return The event with the specified name, or {@code null} if no such event exists.
     */
    public AgentEvent getEvent(String name) {
        return eventsMap.get(name);
    }

    /**
     * Checks whether an event with the specified name exists in the collection.
     *
     * @param name The name of the event.
     * @return {@code true} if the event exists, {@code false} otherwise.
     */
    public boolean containsEvent(String name) {
        return eventsMap.containsKey(name);
    }

    /**
     * Retrieves an event by its index in the ordered list.
     *
     * @param index The index of the event.
     * @return The event at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public AgentEvent getEventByIndex(int index) {
        return eventsList.get(index);
    }

    /**
     * Retrieves a list of all events in the collection.
     *
     * @return A list of all events.
     */
    public List<AgentEvent> getEventsList() {
        return eventsList;
    }

    /**
     * Retrieves the total number of events in the collection.
     *
     * @return The number of events.
     */
    public int getEventCount() {
        return eventsList.size();
    }

    /**
     * Runs all events in the collection that are triggered.
     * Each event's {@code isTriggered()} method is checked before running its {@code run()} method.
     */
    public void run() {
        for (AgentEvent event : eventsList) {
            if (event.isTriggered()) {
                event.run();
            }
        }
    }

    /**
     * Applies a specified action to each event in the collection.
     *
     * @param action A {@link Consumer} representing the action to be performed on each event.
     */
    public void forEach(Consumer<AgentEvent> action) {
        for (AgentEvent event : eventsList) {
            action.accept(event);
        }
    }

    /**
     * Retrieves a list of all event names in the collection.
     *
     * @return A list of event names.
     */
    public List<String> getEventNames() {
        return new ArrayList<>(eventsMap.keySet());
    }

    /**
     * Checks whether a specific event is triggered.
     *
     * @param eventName The name of the event.
     * @return {@code true} if the event is triggered, {@code false} if not, or {@code null} if the event does not exist.
     */
    public Boolean isEventTriggered(String eventName) {
        AgentEvent event = eventsMap.get(eventName);
        if (event == null) {
            return null; // Event not found.
        }
        return event.isTriggered();
    }
}
