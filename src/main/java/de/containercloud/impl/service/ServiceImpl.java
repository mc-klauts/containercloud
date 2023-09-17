package de.containercloud.impl.service;

import de.containercloud.api.service.Service;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ServiceImpl implements Service {

    private final UUID uid;
    private final String serviceName;
    private final ServiceConfigurationImpl serviceConfiguration;

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public UUID serviceId() {
        return this.uid;
    }

    @Override
    public String serviceName() {
        return this.serviceName;
    }

    @Override
    public ServiceConfiguration configuration() {
        return this.serviceConfiguration;
    }
}
