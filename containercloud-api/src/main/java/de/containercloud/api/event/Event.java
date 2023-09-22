package de.containercloud.api.event;

public interface Event {

    String eventType();

    void onTrigger(EventData data);
}
