package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CloudPullImages {
    private final DockerClient dockerClient;
    private final Logger logger = LoggerFactory.getLogger("Docker Pull");

    public CloudPullImages(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void pullImage(String identifier) {
        this.logger.info("Pulling image " + identifier + "...");
        try {
            this.dockerClient.pullImageCmd(identifier).start().awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
