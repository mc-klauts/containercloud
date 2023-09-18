package de.containercloud.impl.service;

import de.containercloud.api.service.Service;
import de.containercloud.api.task.Task;
import de.containercloud.database.MongoProvider;
import de.containercloud.wrapper.CloudWrapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceImpl implements Service {

    private final String serviceId;
    private final String taskId;
    private final String serviceName;

    @Override
    public boolean start() {
        return CloudWrapper.getINSTANCE().getContainerWrapper().startService(this.serviceId);
    }

    @Override
    public boolean stop() {
        return CloudWrapper.getINSTANCE().getContainerWrapper().stopService(this.serviceId);
    }

    @Override
    public boolean remove() {
        return CloudWrapper.getINSTANCE().getContainerWrapper().removeService(this.serviceId);
    }

    @Override
    public Task task() {
        return MongoProvider.getINSTANCE().getTaskHandler().task(taskId);
    }

    @Override
    public String serviceId() {
        return this.serviceId;
    }

    @Override
    public String serviceName() {
        return this.serviceName;
    }
}
