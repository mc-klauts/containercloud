package de.dasshorty.shortcloud;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import de.dasshorty.shortcloud.json.JsonBodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ShortCloudInstance {

    private final Logger logger;
    private DockerHttpClient httpClient;
    private DockerClientConfig dockerClientConfig;

    public ShortCloudInstance() {

        this.logger = LoggerFactory.getLogger("ShortCloud");

        initDockerClientConfig();
        initHttpClient();
        checkDockerConnection();
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
