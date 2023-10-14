package de.containercloud.impl.service;

import com.mongodb.lang.NonNull;
import de.containercloud.api.service.Service;
import de.containercloud.api.service.ServiceBuilder;
import de.containercloud.api.service.ServiceManager;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.task.TaskImpl;
import de.containercloud.wrapper.CloudWrapper;

import java.util.concurrent.ExecutionException;

public class ServiceManagerImpl implements ServiceManager {
    @Override
    public Service withName(@NonNull String serviceName) {
        try {
            return MongoProvider.getINSTANCE().getServiceHandler().service(serviceName).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Service withId(String serviceId) {
        try {
            return MongoProvider.getINSTANCE().getServiceHandler().serviceById(serviceId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Service createService(ServiceBuilder builder) {
        return CloudWrapper.getINSTANCE().getContainerWrapper().runService((TaskImpl) builder.task());
    }

    @Override
    public boolean removeService(String serviceId) {
        return CloudWrapper.getINSTANCE().getContainerWrapper().removeService(serviceId);
    }
}
