package de.containercloud.api.task;

import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.template.Template;

public interface Task {

    ServiceConfiguration configuration();

    /**
     * @return template from the current task or null if the template can't be found in the database
     */
    Template template();

    /**
     * @param template the new template for the task
     * @return if the template update was successfully
     */
    boolean template(Template template);

    String taskId();

    int runningServices();


}
