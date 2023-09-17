package de.containercloud.impl.service;

import de.containercloud.api.service.configuration.ServiceConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceConfigurationImpl implements ServiceConfiguration {

    private final int port;

    @Override
    public int runningPort() {
        return this.port;
    }
}
