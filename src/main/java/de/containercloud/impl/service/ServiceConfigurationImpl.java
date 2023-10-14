package de.containercloud.impl.service;

import com.google.gson.Gson;
import de.containercloud.api.MinecraftVersion;
import de.containercloud.api.service.configuration.PortPair;
import de.containercloud.api.service.configuration.ServiceConfiguration;


public class ServiceConfigurationImpl implements ServiceConfiguration {
    private final PortPair port;
    private final MinecraftVersion minecraftVersion;
    private final int maxServices;
    public ServiceConfigurationImpl(PortPair port, MinecraftVersion minecraftVersion, int maxServices) {
        this.port = port;
        this.minecraftVersion = minecraftVersion;
        this.maxServices = maxServices;
    }

    @Override
    public PortPair runningPort() {
        return this.port;
    }

    @Override
    public MinecraftVersion version() {
        return this.minecraftVersion;
    }

    @Override
    public int maxServices() {
        return maxServices;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
