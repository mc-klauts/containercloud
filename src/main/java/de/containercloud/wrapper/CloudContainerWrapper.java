package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.mongodb.client.model.Filters;
import de.containercloud.api.service.Service;
import de.containercloud.api.task.Task;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.database.MongoProvider;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.service.ServiceImpl;
import de.containercloud.impl.task.TaskImpl;
import de.containercloud.shutdown.ShutdownService;
import de.containercloud.web.socket.services.ListServices;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudContainerWrapper {

    private final DockerClient dockerClient;
    private final Map<String, Service> runningContainers = new HashMap<>();
    private final MongoDatabaseHandler databaseHandler;

    public CloudContainerWrapper(DockerClient dockerClient, MongoDatabaseHandler databaseHandler) {
        this.dockerClient = dockerClient;
        this.databaseHandler = databaseHandler;

        ShutdownService.addShutdown(1, o -> {
            runningContainers.keySet().forEach(s -> {

                val taskId = runningContainers.get(s).task().taskId();

                val task = MongoProvider.getINSTANCE().getTaskHandler().task(taskId);

                MongoProvider.getINSTANCE().getTaskHandler().updateTask(task);

                ShutdownService.addShutdown(2, o1 -> {
                    this.dockerClient.stopContainerCmd(s).exec();
                    this.dockerClient.removeContainerCmd(s).exec();
                });

            });

            runningContainers.clear();

        });
    }

    public List<String> listRunningContainers() {
        return new ArrayList<>(runningContainers.keySet());
    }

    private @NotNull String createServiceName(Task task) {

        val collection = EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.SERVICE);

        this.databaseHandler.collection(collection).find(Filters.eq(""));

        return task.taskId() + "-" + System.currentTimeMillis();
    }

    public ServiceImpl runService(TaskImpl task) {

        val serviceName = createServiceName(task);

        val createContainer = this.dockerClient.createContainerCmd("itzg/minecraft-server:latest")
                .withName("cloud-" + serviceName)
                .withEnv("TYPE=" + task.configuration().version().platform().getPlatformId(),
                        "EULA=true");

        createContainer
                .getHostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPortRange(task.configuration().runningPort().from(), task.configuration().runningPort().to()), ExposedPort.tcp(25565)));

        val response = createContainer.withVolumes()
                .exec();


        val id = response.getId();

        MongoProvider.getINSTANCE().getTaskHandler().updateTask(task);

        this.dockerClient.startContainerCmd(id).exec();

        val service = new ServiceImpl(id, task.taskId(), serviceName);

        this.runningContainers.put(id, service);

        ListServices.broadcastServiceUpdate(this.runningContainers.values().stream().toList());

        return service;
    }

    public boolean stopService(String serviceId) {

        if (!this.runningContainers.containsKey(serviceId))
            return false;

        this.dockerClient.stopContainerCmd(serviceId).exec();
        return true;
    }

    public boolean removeService(String serviceId) {

        if (!this.runningContainers.containsKey(serviceId))
            return false;

        this.dockerClient.removeServiceCmd(serviceId).exec();
        return true;
    }

    public boolean startService(String serviceId) {
        if (!this.runningContainers.containsKey(serviceId))
            return false;

        this.dockerClient.startContainerCmd(serviceId).exec();
        return true;
    }

}
