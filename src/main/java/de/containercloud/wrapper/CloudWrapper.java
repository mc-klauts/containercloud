package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import de.containercloud.api.event.EventManager;
import de.containercloud.api.service.ServiceManager;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.impl.event.EventManagerImpl;
import de.containercloud.impl.service.ServiceManagerImpl;
import de.containercloud.registry.CloudRegistryImpl;
import de.containercloud.web.CloudSocketServer;
import lombok.Getter;
import org.slf4j.Logger;

@Getter
public class CloudWrapper {

    @Getter
    private static CloudWrapper INSTANCE;
    private final DockerClient dockerClient;
    private final CloudContainerWrapper containerWrapper;
    private final CloudSocketServer socketServer;
    private final MongoDatabaseHandler databaseHandler;
    private final ServiceManagerImpl serviceManager;
    private final EventManagerImpl eventManager;

    public CloudWrapper(DockerHttpClient httpClient, DockerClientConfig dockerClientConfig, Logger logger, MongoDatabaseHandler databaseHandler, CloudRegistryImpl registry) {
        this.databaseHandler = databaseHandler;

        INSTANCE = this;

        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient);

        logger.info("Starting Cloud Wrapper...");

        this.containerWrapper = new CloudContainerWrapper(this.dockerClient, this.databaseHandler);
        this.socketServer = new CloudSocketServer();

        this.serviceManager = new ServiceManagerImpl();

        registry.addService(ServiceManager.class, serviceManager);

        this.eventManager = new EventManagerImpl();

        registry.addService(EventManager.class, eventManager);
    }


}
