package de.containercloud.api.service.configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultServiceConfiguration implements ServiceConfiguration {
    int runningPort;

    @Override
    public int runningPort() {
        return runningPort;
    }
}
