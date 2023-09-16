package de.containercloud.api.service;

import de.containercloud.api.ServiceType;

import java.util.List;

public interface ServiceManager {

    Service createService(ServiceConfiguration configuration);
    List<Service> runningServices();
    List<Service> runningServices(ServiceType withType);

}
