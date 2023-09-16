package de.containercloud.api;

public interface Volume {
    boolean cleanUp();

    MinecraftVersion version();
}
