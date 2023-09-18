package de.containercloud.api.service;

import lombok.NonNull;

import java.util.UUID;

public interface ServiceManager {

    Service withName(@NonNull String serviceName);

    Service withId(String serviceId);

    Service createService();

    boolean removeService(String serviceId);

}
