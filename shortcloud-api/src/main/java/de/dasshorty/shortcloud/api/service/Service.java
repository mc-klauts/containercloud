package de.dasshorty.shortcloud.api.service;

/*
 * Represents a dockerized minecraft server
 * */
public interface Service {

    boolean start();

    boolean stop();

    boolean remove();

    ServiceConfiguration configuration();

}
