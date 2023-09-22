package de.containercloud.api.service.configuration;

import de.containercloud.api.MinecraftVersion;

public interface ServiceConfiguration {

    /**
     * @return the port range to the task
     * */
    PortPair runningPort();

    /**
     * @see MinecraftVersion for detailed description
     * */
    MinecraftVersion version();

}
