package de.containercloud.api.service.configuration;

import de.containercloud.api.MinecraftVersion;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultServiceConfiguration implements ServiceConfiguration {
    private final PortPair runningPort;
    private final MinecraftVersion minecraftVersion;

    @Override
    public PortPair runningPort() {
        return runningPort;
    }

    @Override
    public MinecraftVersion version() {
        return this.minecraftVersion;
    }
}
