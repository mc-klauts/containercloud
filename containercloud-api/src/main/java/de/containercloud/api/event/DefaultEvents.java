package de.containercloud.api.event;

public enum DefaultEvents {
    ON_SERVICE_CREATE("OnServiceCreate"),
    ON_SERVICE_DELETE("OnServiceDelete"),
    ON_SERVICE_START("OnServiceStart"),
    ON_SERVICE_STOP("OnServiceStop");

    String key;

    DefaultEvents(String key) {
        this.key = key;
    }
}
