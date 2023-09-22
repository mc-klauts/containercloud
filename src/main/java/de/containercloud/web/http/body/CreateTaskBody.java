package de.containercloud.web.http.body;

import de.containercloud.api.ServiceType;
import de.containercloud.impl.service.ServiceConfigurationImpl;

import java.util.List;

public record CreateTaskBody(ServiceType type, String taskId, List<String> runningServices,
                             ServiceConfigurationImpl serviceConfiguration, String template) {
}
