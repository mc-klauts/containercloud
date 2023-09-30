package de.containercloud.api.service;

import de.containercloud.api.task.Task;

/*
 * Represents a dockerized minecraft server
 * */
public interface Service {

    /**
     * Start a Service
     * @return if the start was successfully
     * */
    boolean start();
    /**
     * Stop a Service
     * @return if the stop was successfully
     * */
    boolean stop();
    /**
     * remove a Service complete also deletes volume if created
     * @return if the remove was successfully
     * */
    boolean remove();

    Task task();

    String serviceId();

    String serviceName();

    ServiceVolume data();

}
