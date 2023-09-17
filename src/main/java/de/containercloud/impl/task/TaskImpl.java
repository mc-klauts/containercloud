package de.containercloud.impl.task;

import de.containercloud.api.ServiceType;
import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.task.Task;
import de.containercloud.api.template.Template;
import de.containercloud.database.MongoProvider;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TaskImpl implements Task {

    private final ServiceType type;
    private final UUID uid;
    private final List<UUID> runningServices;
    private String template;

    @Override
    public UUID uid() {
        return this.uid;
    }

    @Override
    public ServiceType type() {
        return type;
    }

    @Override
    public List<UUID> runningServices() {
        return this.runningServices;
    }

    @Override
    public boolean createService(ServiceConfiguration configuration) {
        return false;
    }

    @Override
    public boolean removeService(UUID uuid) {
        return false;
    }

    @Override
    public Template template() {
        return MongoProvider.getINSTANCE().getTemplateHandler().template(this.template);
    }

    @Override
    public boolean template(Template template) {
        this.template = template().name();
        return false;
    }
}
