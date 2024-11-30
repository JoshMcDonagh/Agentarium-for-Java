package agents.attributes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int getEventCount() {
        return eventsList.size();
    }

    public void run() {
        for (AgentEvent event : eventsList) {
            if (event.isTriggered())
                event.run();
        }
    }
}
