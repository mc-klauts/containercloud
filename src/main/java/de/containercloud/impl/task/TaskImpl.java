package de.containercloud.impl.task;

import com.google.gson.Gson;
import de.containercloud.api.ServiceType;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.task.Task;
import de.containercloud.api.template.Template;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.service.ServiceConfigurationImpl;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TaskImpl implements Task {

    private final String taskId;
    private final List<String> runningServices;
    private final ServiceConfigurationImpl serviceConfiguration;
    private String template;

    @Override
    public String taskId() {
        return this.taskId;
    }

    @Override
    public List<String> runningServices() {
        return this.runningServices;
    }

    @Override
    public ServiceConfiguration configuration() {
        return this.serviceConfiguration;
    }

    @Override
    public Template template() {
        return MongoProvider.getINSTANCE().getTemplateHandler().template(this.template);
    }

    @Override
    public boolean template(Template template) {
        this.template = template().name();
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
