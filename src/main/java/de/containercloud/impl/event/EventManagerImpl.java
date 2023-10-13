package de.containercloud.impl.event;

import de.containercloud.api.event.Event;
import de.containercloud.api.event.GsonData;
import de.containercloud.api.event.EventManager;
import de.containercloud.api.event.EventType;

import java.util.ArrayList;
import java.util.List;

public class EventManagerImpl implements EventManager {
    private List<Event> events;

    public EventManagerImpl() {
        this.events = new ArrayList<>();
    }

    @Override
    public void register(Event event) {
        this.events.add(event);
    }

    public void callEvent(EventType eventType, GsonData gsonData) {
        for (Event event : events) {
            if(event.eventType().equals(eventType)) {
                event.onTrigger(gsonData);
            }
        }
    }
}
