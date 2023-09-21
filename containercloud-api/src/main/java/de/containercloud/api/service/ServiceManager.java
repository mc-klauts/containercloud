package de.containercloud.api.service;

import de.containercloud.api.registry.CloudService;
import lombok.NonNull;

import java.util.UUID;

public interface ServiceManager extends CloudService {

    Service withName(@NonNull String serviceName);

    Service withId(String serviceId);

    Service createService(ServiceBuilder builder);

    boolean removeService(String serviceId);

}
