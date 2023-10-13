package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import de.containercloud.api.service.Service;
import de.containercloud.api.task.Task;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.service.ServiceImpl;
import de.containercloud.impl.task.TaskImpl;
import de.containercloud.protocol.socket.services.ListServices;
import de.containercloud.shutdown.ShutdownService;
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
        return task.taskId() + "-" + task.runningServices() + 1;
    }

    public ServiceImpl runService(TaskImpl task) {

        val serviceName = createServiceName(task);

        // create container volume
        val containerVolume = new Volume("/data");

        Bind bind = new Bind("/home/theccloud/services/" + serviceName, containerVolume)

        // TODO - specify custom docker image
        val createContainer = this.dockerClient.createContainerCmd("itzg/minecraft-server:latest")

                .withName("cloud-" + serviceName)

                .withHostConfig(HostConfig.newHostConfig()

                        .withPortBindings(new PortBinding(
                                Ports.Binding.bindPortRange(
                                        task.configuration().runningPort().from(),
                                        task.configuration().runningPort().to()
                                ), ExposedPort.tcp(25565)))

                        .withBinds(bind))

                .withEnv("TYPE=" + task.configuration().version().platform().getPlatformId(),
                        "EULA=true");

        // TODO - make mount path changeable


        // TODO copy files from template

        createContainer.withVolumes(containerVolume);


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
