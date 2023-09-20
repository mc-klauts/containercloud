package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import de.containercloud.websocket.CloudSocketServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class CloudWrapper {

    @Getter
    private static CloudWrapper INSTANCE;
    private final DockerClient dockerClient;
    private final CloudContainerWrapper containerWrapper;
    private final CloudSocketServer socketServer;

    public CloudWrapper(DockerHttpClient httpClient, DockerClientConfig dockerClientConfig, Logger logger) {

        INSTANCE = this;

        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient);

        logger.info("Starting Cloud Wrapper...");

        this.containerWrapper = new CloudContainerWrapper(this.dockerClient);
        this.socketServer = new CloudSocketServer();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            this.dockerClient.pingCmd().exec();
        }, 10, TimeUnit.SECONDS);
    }


}
