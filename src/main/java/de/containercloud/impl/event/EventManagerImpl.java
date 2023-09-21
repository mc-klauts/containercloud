package de.containercloud.impl.event;

import de.containercloud.api.event.Event;
import de.containercloud.api.event.EventData;
import de.containercloud.api.event.EventManager;

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

    @Override
    public void callEvent(String eventType, EventData eventData) {
        for (Event event : events) {
            if(event.eventType().equals(eventType)) {
                event.onTrigger(eventData);
            }
        }
    }
}
