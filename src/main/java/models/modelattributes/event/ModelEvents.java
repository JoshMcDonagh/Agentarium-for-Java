package models.modelattributes.event;

import agents.attributes.event.AgentEvent;
import models.modelattributes.ModelAttributeSet;
import models.multithreading.threadutilities.AgentStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelEvents {
    private final Map<String, ModelEvent> eventsMap = new HashMap<String, ModelEvent>();
    private final List<ModelEvent> eventsList = new ArrayList<ModelEvent>();

    private Map<String, ModelAttributeSet> modelAttributeSetMap;
    private AgentStore agentStore;

    public void setModelAttributeSets(Map<String, ModelAttributeSet> modelAttributeSetList) {
        this.modelAttributeSetMap = modelAttributeSetList;
    }

    public void setAgentStore(AgentStore agentStore) {
        this.agentStore = agentStore;
    }

    public void addEvent(ModelEvent newEvent) {
        newEvent.setModelAttributeSets(modelAttributeSetMap);
        newEvent.setAgentStore(agentStore);
        eventsMap.put(newEvent.name(), newEvent);
        eventsList.add(newEvent);
    }

    public void addEvents(List<ModelEvent> newEvents) {
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

    public List<ModelEvent> getEventsList() {
        return eventsList;
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
