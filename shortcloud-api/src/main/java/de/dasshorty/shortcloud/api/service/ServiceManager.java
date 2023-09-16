package de.dasshorty.shortcloud.api.service;

import de.dasshorty.shortcloud.api.ServiceType;

import java.util.List;

public interface ServiceManager {

    Service createService(ServiceConfiguration configuration);
    List<Service> runningServices();
    List<Service> runningServices(ServiceType withType);

}
