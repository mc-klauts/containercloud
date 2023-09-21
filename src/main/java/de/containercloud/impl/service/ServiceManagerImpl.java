package de.containercloud.impl.service;

import de.containercloud.api.service.Service;
import de.containercloud.api.service.ServiceBuilder;
import de.containercloud.api.service.ServiceManager;
import de.containercloud.database.MongoProvider;
import de.containercloud.wrapper.CloudWrapper;
import lombok.NonNull;

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
    public Service createService(ServiceBuilder builder) {
        return CloudWrapper.getINSTANCE().getContainerWrapper().runService(builder.task());
    }

    @Override
    public boolean removeService(String serviceId) {
        return CloudWrapper.getINSTANCE().getContainerWrapper().removeService(serviceId);
    }
}
