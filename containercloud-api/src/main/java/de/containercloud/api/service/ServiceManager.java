package de.containercloud.api.service;

import de.containercloud.api.registry.CloudService;

public interface ServiceManager extends CloudService {

    Service withName(String serviceName);

    Service withId(String serviceId);

    Service createService(ServiceBuilder builder);

    boolean removeService(String serviceId);

}
