package de.containercloud.impl.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.containercloud.api.service.Service;
import de.containercloud.api.task.Task;
import de.containercloud.database.MongoProvider;
import de.containercloud.wrapper.CloudWrapper;

import java.util.concurrent.ExecutionException;

public class ServiceImpl implements Service {
    @JsonSerialize
    private final String serviceId;
    @JsonSerialize
    private final String taskId;
    @JsonSerialize
    private final String serviceName;

    public ServiceImpl(String serviceId, String taskId, String serviceName) {
        this.serviceId = serviceId;
        this.taskId = taskId;
        this.serviceName = serviceName;
    }

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
        try {
            return MongoProvider.getINSTANCE().getTaskHandler().task(taskId).get();
        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }
        return null;
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
