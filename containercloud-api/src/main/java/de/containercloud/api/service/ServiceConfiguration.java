package de.containercloud.api.service;

import java.net.InetSocketAddress;

public interface ServiceConfiguration {

    int runningPort();
    InetSocketAddress runningAddress();

}
