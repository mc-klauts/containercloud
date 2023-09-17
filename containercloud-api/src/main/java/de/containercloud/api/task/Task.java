package de.containercloud.api.task;

import de.containercloud.api.ServiceType;
import de.containercloud.api.service.Service;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.template.Template;

import java.util.List;
import java.util.UUID;

public interface Task {

    ServiceType type();

    List<Service> runningServices();

    boolean createService(ServiceConfiguration configuration);

    boolean removeService(UUID uuid);

    Template template();

    boolean template(Template template);


}
