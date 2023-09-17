package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.val;
import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CloudWrapper {

    private final DockerClient dockerClient;

    public CloudWrapper(DockerHttpClient httpClient, DockerClientConfig dockerClientConfig, Logger logger) {
        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient);
    }
}
