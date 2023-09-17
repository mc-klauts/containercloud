package de.containercloud.impl.task;

import de.containercloud.api.ServiceType;
import de.containercloud.api.service.Service;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.task.Task;

import java.util.List;
import java.util.UUID;

public class TaskImpl implements Task {

    private final ServiceType type;

    public TaskImpl(ServiceType type) {
        this.type = type;
    }



    @Override
    public ServiceType type() {
        return type;
    }

    @Override
    public List<Service> runningServices() {
        return null;
    }

    @Override
    public boolean createService(ServiceConfiguration configuration) {
        return false;
    }

    @Override
    public boolean removeService(UUID uuid) {
        return false;
    }
}
