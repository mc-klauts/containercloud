package de.containercloud.impl.service;

import de.containercloud.api.service.Service;
import de.containercloud.api.service.ServiceManager;
import de.containercloud.database.MongoProvider;
import lombok.NonNull;

import java.util.UUID;

public class ServiceManagerImpl implements ServiceManager {
    @Override
    public Service withName(@NonNull String serviceName) {
        return MongoProvider.getINSTANCE().getServiceHandler().service(serviceName);
    }

    @Override
    public Service withId(String serviceId) {
        return MongoProvider.getINSTANCE().getServiceHandler().serviceById(serviceId);
    }

    @Override
    public Service createService() {
        return null;
    }

    @Override
    public boolean removeService(String serviceId) {
        return false;
    }
}
