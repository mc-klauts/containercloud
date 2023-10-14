package de.containercloud.impl.task;

import com.google.gson.Gson;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.task.Task;
import de.containercloud.api.template.Template;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.service.ServiceConfigurationImpl;

import java.util.concurrent.ExecutionException;

public class TaskImpl implements Task {
    private final String taskId;
    private final ServiceConfigurationImpl serviceConfiguration;
    private String template;
    private int runningServices = 0;

    public TaskImpl(String taskId, ServiceConfigurationImpl serviceConfiguration, String template, int runningServices) {
        this.taskId = taskId;
        this.serviceConfiguration = serviceConfiguration;
        this.template = template;
        this.runningServices = runningServices;
    }

    public String getTaskId() {
        return taskId;
    }

    public ServiceConfigurationImpl getServiceConfiguration() {
        return serviceConfiguration;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getRunningServices() {
        return runningServices;
    }

    public void setRunningServices(int runningServices) {
        this.runningServices = runningServices;
    }

    @Override
    public String taskId() {
        return this.taskId;
    }

    @Override
    public int runningServices() {
        return runningServices;
    }

    @Override
    public ServiceConfiguration configuration() {
        return this.serviceConfiguration;
    }

    @Override
    public Template template() {
        try {
            return MongoProvider.getINSTANCE().getTemplateHandler().template(this.template).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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
