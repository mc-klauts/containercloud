package de.containercloud;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import de.containercloud.api.CloudAPI;
import de.containercloud.config.ConfigHandler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.json.JsonBodyHandler;
import de.containercloud.registry.CloudRegistryImpl;
import de.containercloud.wrapper.CloudWrapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public class ContainerCloudInstance {

    private final Logger logger;
    private DockerHttpClient httpClient;
    private DockerClientConfig dockerClientConfig;

    public ContainerCloudInstance() {

        this.logger = LoggerFactory.getLogger("ContainerCloud");
        initDockerClientConfig();
        initHttpClient();
        checkDockerConnection();
        healthCheck();

        val configHandler = new ConfigHandler(this.logger);

        MongoDatabaseHandler databaseHandler = new MongoDatabaseHandler(configHandler);

        val registry = new CloudRegistryImpl();
        new CloudAPI(registry);

        CloudWrapper wrapper = new CloudWrapper(this.httpClient, this.dockerClientConfig, this.logger, databaseHandler, registry);

        addShutDownHookForHttpClient();
    }

    private void healthCheck() {
        Request request = Request.builder()
                .method(Request.Method.GET)
                .path("/_ping")
                .build();
        try (Response response = this.httpClient.execute(request)) {

            this.logger.info("--- HEALTH CHECK ---");
            this.logger.info("Status: " + response.getStatusCode());
            this.logger.info("Message: " + JsonBodyHandler.toJson(response.getBody()));
            this.logger.info("--- HEALTH CHECK ---");
        }
    }

    private void addShutDownHookForHttpClient() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.httpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private void initDockerClientConfig() {
        this.dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .withDockerTlsVerify(true)
                .withDockerCertPath("/home/user/.docker")
                .build();
    }

    private void checkDockerConnection() {

        Request request = Request.builder()
                .method(Request.Method.GET)
                .path("/_ping")
                .build();
        try (Response response = this.httpClient.execute(request)) {

            if (response.getStatusCode() == 200) {
                this.logger.info("Established a connection to Docker!");
                this.logger.info("Docker respond with OK");
            } else {
                this.logger.error("Something went wrong!");
                this.logger.error("Docker respond with " + response.getStatusCode());
                this.logger.debug(JsonBodyHandler.toJson(response.getBody()));
            }
        }
    }

    private void initHttpClient() {

        this.httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(this.dockerClientConfig.getDockerHost())
                .sslConfig(this.dockerClientConfig.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

    }
}
