package de.containercloud.api.event;

import de.containercloud.api.registry.CloudService;

public interface EventManager extends CloudService {

    // This Methode can be used to register an Event
    void register(Event event);

    // This Methode is for calling an Event
    void callEvent(String eventType, EventData eventData);
}
