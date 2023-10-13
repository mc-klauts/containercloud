package de.containercloud.api.event;

public interface Event {

    EventType eventType();

    void onTrigger(GsonData data);
}
