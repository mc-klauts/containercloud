package de.containercloud.api;

public enum ServiceType{

    NORMAL,
    DYNAMIC,
    PROXY;

    public String toString() {
        return this.name();
    }

}
