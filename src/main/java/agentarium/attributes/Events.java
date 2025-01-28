package agentarium.attributes;

import java.util.List;
import java.util.Map;

public class Events {
    private Map<String, Integer> eventIndexes;
    private List<Event> events;

    public void add(Event event) {
        Integer index = events.size();
        eventIndexes.put(event.getName(), index);
        events.add(event);
    }

    public void add(List<Event> events) {
        for (Event event : events)
            add(event);
    }

    public Event get(String name) {
        Integer index = eventIndexes.get(name);
        return events.get(index);
    }

    public Event get(int index) {
        return events.get(index);
    }

    public int size() {
        return events.size();
    }

    public void run() {
        for (Event event : events) {
            if (event.isTriggered())
                event.run();
        }
    }
}
