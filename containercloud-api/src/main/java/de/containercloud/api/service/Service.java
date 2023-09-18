package de.containercloud.api.service;

import de.containercloud.api.service.configuration.ServiceConfiguration;
import de.containercloud.api.task.Task;

import java.util.UUID;

/*
 * Represents a dockerized minecraft server
 * */
public interface Service {

    boolean start();

    boolean stop();

    boolean remove();

    Task task();

    UUID serviceId();

    String serviceName();

}
