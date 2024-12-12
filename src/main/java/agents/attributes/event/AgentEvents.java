package agents.attributes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AgentEvents {
    private final Map<String, AgentEvent> eventsMap = new HashMap<String, AgentEvent>();
    private final List<AgentEvent> eventsList = new ArrayList<AgentEvent>();

    public void addEvent(AgentEvent newEvent) {
        eventsMap.put(newEvent.name(), newEvent);
        eventsList.add(newEvent);
    }

    public void addEvents(AgentEvent[] newEvents) {
        for (AgentEvent newEvent : newEvents) {
            addEvent(newEvent);
        }
    }

    public AgentEvent getEvent(String name) {
        return eventsMap.get(name);
    }

    public boolean containsEvent(String name) {
        return eventsMap.containsKey(name);
    }

    public AgentEvent getEventByIndex(int index) {
        return eventsList.get(index);
    }

    public List<AgentEvent> getEventsList() {
        return eventsList;
    }

    public int getEventCount() {
        return eventsList.size();
    }

    public void run() {
        for (AgentEvent event : eventsList) {
            if (event.isTriggered())
                event.run();
        }
    }

    public void forEach(Consumer<AgentEvent> action) {
        for (AgentEvent event : eventsList) {
            action.accept(event);
        }
    }

    public List<String> getEventNames() {
        return new ArrayList<>(eventsMap.keySet());
    }


    public Boolean isEventTriggered(String eventName) {
        AgentEvent event = eventsMap.get(eventName);
        if (event == null)
            return null;
        return event.isTriggered();
    }
}
