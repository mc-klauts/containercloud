package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class CloudPullImages {

    private final DockerClient dockerClient;
    private final Logger logger = LoggerFactory.getLogger("Docker Pull");

    @SneakyThrows
    public void pullImage(String identifier) {
        this.logger.info("Pulling image " + identifier + "...");
        this.dockerClient.pullImageCmd(identifier).start().awaitCompletion();
    }
}
