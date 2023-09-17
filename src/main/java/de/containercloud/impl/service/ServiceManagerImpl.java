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
    public Service withId(UUID uid) {
        return MongoProvider.getINSTANCE().getServiceHandler().service(uid);
    }
}
