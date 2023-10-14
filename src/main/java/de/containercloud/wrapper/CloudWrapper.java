package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import de.containercloud.api.event.EventManager;
import de.containercloud.api.service.ServiceManager;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.event.EventManagerImpl;
import de.containercloud.impl.service.ServiceManagerImpl;
import de.containercloud.protocol.CloudSocketServer;
import de.containercloud.protocol.packet.PacketHandler;
import de.containercloud.registry.CloudRegistryImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;

public class CloudWrapper {

    private static CloudWrapper INSTANCE;
    private final DockerClient dockerClient;
    private final CloudContainerWrapper containerWrapper;
    private final CloudSocketServer socketServer;
    private final MongoDatabaseHandler databaseHandler;
    private final ServiceManagerImpl serviceManager;
    private final EventManagerImpl eventManager;
    private final PacketHandler packetHandler;

    public CloudWrapper(DockerHttpClient httpClient, DockerClientConfig dockerClientConfig, @NotNull Logger logger, MongoDatabaseHandler databaseHandler, @NotNull CloudRegistryImpl registry) {
        this.databaseHandler = databaseHandler;

        INSTANCE = this;

        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, httpClient);

        logger.info("Starting Cloud Wrapper...");

        // Start all sub wrapper
        this.containerWrapper = new CloudContainerWrapper(this.dockerClient, MongoProvider.getINSTANCE().getTaskHandler());

        logger.info("Starting Sockets...");
        this.socketServer = new CloudSocketServer();

        // api management
        logger.info("Starting API Services...");

        this.serviceManager = new ServiceManagerImpl();

        registry.addService(ServiceManager.class, serviceManager);

        this.eventManager = new EventManagerImpl();

        registry.addService(EventManager.class, eventManager);

        try {
            this.packetHandler = new PacketHandler(this.getEventManager(), MongoProvider.getINSTANCE().getVerificationHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("CloudWrapper start finished!");

        CloudPullImages cloudPullImages = new CloudPullImages(this.dockerClient);

        cloudPullImages.pullImage("itzg/minecraft-server:latest");
    }

    public static CloudWrapper getINSTANCE() {
        return INSTANCE;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public CloudContainerWrapper getContainerWrapper() {
        return containerWrapper;
    }

    public CloudSocketServer getSocketServer() {
        return socketServer;
    }

    public MongoDatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public ServiceManagerImpl getServiceManager() {
        return serviceManager;
    }

    public EventManagerImpl getEventManager() {
        return eventManager;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }


}
