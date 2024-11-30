package models.modelattributes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelEvents {
    private final Map<String, ModelEvent> eventsMap = new HashMap<String, ModelEvent>();
    private final List<ModelEvent> eventsList = new ArrayList<ModelEvent>();

    public void addEvent(ModelEvent newEvent) {
        eventsMap.put(newEvent.name(), newEvent);
        eventsList.add(newEvent);
    }

    public void addEvents(ModelEvent[] newEvents) {
        for (ModelEvent newEvent : newEvents)
            addEvent(newEvent);
    }

    public ModelEvent getEvent(String eventName) {
        return eventsMap.get(eventName);
    }

    public boolean containsEvent(String eventName) {
        return eventsMap.containsKey(eventName);
    }

    public ModelEvent getEventByIndex(int index) {
        return eventsList.get(index);
    }

    public int getEventCount() {
        return eventsList.size();
    }

    public void run() {
        for (ModelEvent event : eventsList) {
            if (event.isTriggered())
                event.run();
        }
    }
}
