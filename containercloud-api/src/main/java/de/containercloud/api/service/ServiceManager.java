package de.containercloud.api.service;

import lombok.NonNull;

import java.util.UUID;

public interface ServiceManager {

    Service withName(@NonNull String serviceName);

    Service withId(UUID uid);

}
