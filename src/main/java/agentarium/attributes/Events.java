package agentarium.attributes;

import java.util.List;
import java.util.Map;

public class Events extends Attributes {
    private Map<String, Integer> eventIndexes;
    private List<Event> events;

    public void add(Event event) {
        addAttribute(event);
    }

    public void add(List<Event> events) {
        for (Event event : events)
            add(event);
    }

    public Event get(String name) {
        return (Event) getAttribute(name);
    }

    public Event get(int index) {
        return (Event) getAttribute(index);
    }

    public void run() {
        for (int i = 0; i < size(); i++) {
            Event event = get(i);
            if (event.isTriggered())
                event.run();
        }
    }
}
