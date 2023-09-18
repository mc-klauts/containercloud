package de.containercloud.api.task;

import de.containercloud.api.ServiceType;
import de.containercloud.api.service.Service;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.template.Template;

import java.util.List;
import java.util.UUID;

public interface Task {

    ServiceType type();

    List<UUID> runningServices();

    Service createService();

    boolean removeService(UUID uuid);

    TaskVolume volume();

    ServiceConfiguration configuration();

    /**
     * @return template from the current task or null if the template can't be found in the database
     * */
    Template template();

    boolean template(Template template);

    UUID uid();


}
