package de.containercloud.impl.service;

import com.google.gson.Gson;
import de.containercloud.api.MinecraftVersion;
import de.containercloud.api.service.configuration.PortPair;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceConfigurationImpl implements ServiceConfiguration {

    private final PortPair port;
    private final MinecraftVersion minecraftVersion;

    @Override
    public PortPair runningPort() {
        return this.port;
    }

    @Override
    public MinecraftVersion version() {
        return this.minecraftVersion;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
