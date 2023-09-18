package de.containercloud.api.service.configuration;

import de.containercloud.api.MinecraftVersion;

public interface ServiceConfiguration {

    int runningPort();
    MinecraftVersion version();

}
