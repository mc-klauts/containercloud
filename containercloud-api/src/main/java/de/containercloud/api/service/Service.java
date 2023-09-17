package de.containercloud.api.service;

import de.containercloud.api.service.configuration.ServiceConfiguration;

import java.util.UUID;

/*
 * Represents a dockerized minecraft server
 * */
public interface Service {

    boolean start();

    boolean stop();

    boolean remove();

    UUID serviceId();

    ServiceConfiguration configuration();

}
