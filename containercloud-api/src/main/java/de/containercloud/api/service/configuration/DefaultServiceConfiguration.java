package de.containercloud.api.service.configuration;

import de.containercloud.api.MinecraftVersion;

public class DefaultServiceConfiguration implements ServiceConfiguration {
    private final PortPair runningPort;
    private final MinecraftVersion minecraftVersion;
    private final int maxServices;
    public DefaultServiceConfiguration(PortPair runningPort, MinecraftVersion minecraftVersion, int maxServices) {
        this.runningPort = runningPort;
        this.minecraftVersion = minecraftVersion;
        this.maxServices = maxServices;
    }

    @Override
    public PortPair runningPort() {
        return runningPort;
    }

    @Override
    public MinecraftVersion version() {
        return this.minecraftVersion;
    }

    @Override
    public int maxServices() {
        return maxServices;
    }
}
