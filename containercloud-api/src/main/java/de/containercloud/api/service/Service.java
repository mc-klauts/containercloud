package de.containercloud.api.service;

import de.containercloud.api.task.Task;

/*
 * Represents a dockerized minecraft server
 * */
public interface Service {

    boolean start();

    boolean stop();

    boolean remove();

    Task task();

    String serviceId();

    String serviceName();

}
