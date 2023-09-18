package de.containercloud.api.task;

import de.containercloud.api.ServiceType;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.template.Template;

import java.util.List;

public interface Task {

    ServiceType type();

    List<String> runningServices();

    ServiceConfiguration configuration();

    /**
     * @return template from the current task or null if the template can't be found in the database
     */
    Template template();

    boolean template(Template template);

    String taskId();


}
