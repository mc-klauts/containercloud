package de.containercloud.impl.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.containercloud.api.service.Service;
import de.containercloud.api.task.Task;
import de.containercloud.database.MongoProvider;
import de.containercloud.wrapper.CloudWrapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceImpl implements Service {

    @JsonSerialize
    private final String serviceId;
    @JsonSerialize
    private final String taskId;
    @JsonSerialize
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
