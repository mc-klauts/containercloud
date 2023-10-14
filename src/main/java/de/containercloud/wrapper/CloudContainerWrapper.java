package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import de.containercloud.api.service.Service;
import de.containercloud.api.task.Task;
import de.containercloud.database.MongoProvider;
import de.containercloud.database.handler.MongoTaskHandler;
import de.containercloud.impl.service.ServiceImpl;
import de.containercloud.impl.task.TaskImpl;
import de.containercloud.protocol.socket.services.ListServices;
import de.containercloud.shutdown.ShutdownService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CloudContainerWrapper {

    private final DockerClient dockerClient;
    private final MongoTaskHandler taskHandler;
    private final Map<String, Service> runningContainers = new HashMap<>();

    public CloudContainerWrapper(DockerClient dockerClient, MongoTaskHandler taskHandler) {
        this.dockerClient = dockerClient;
        this.taskHandler = taskHandler;

        ShutdownService.addShutdown(1, o -> {
            runningContainers.keySet().forEach(s -> {

                String taskId = runningContainers.get(s).task().taskId();

                TaskImpl task = null;
                try {
                    task = MongoProvider.getINSTANCE().getTaskHandler().task(taskId).get();

                    MongoProvider.getINSTANCE().getTaskHandler().updateTask(task);

                    ShutdownService.addShutdown(2, o1 -> {
                        this.dockerClient.stopContainerCmd(s).exec();
                        this.dockerClient.removeContainerCmd(s).exec();
                    });

                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

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

        String serviceName = createServiceName(task);

        // create container volume
        Volume containerVolume = new Volume("/data");

        Bind bind = new Bind("/home/theccloud/services/" + serviceName, containerVolume);

        // TODO - specify custom docker image
        CreateContainerCmd createContainer = this.dockerClient.createContainerCmd("itzg/minecraft-server:latest")

                .withName("cloud-" + serviceName)

                .withHostConfig(HostConfig.newHostConfig()

                        .withPortBindings(new PortBinding(
                                Ports.Binding.bindPortRange(
                                        task.configuration().runningPort().from(),
                                        task.configuration().runningPort().to()
                                ), ExposedPort.tcp(25565)))

                        .withBinds(bind))

                .withEnv("TYPE=" + task.configuration().version().platform().getPlatformId(),
                        "EULA=true",
                        "VALIDATE-TOKEN=123"); // TODO - change token

        // TODO - make mount path changeable


        createContainer.withVolumes(containerVolume);

        CreateContainerResponse response = createContainer.exec();


        String dockerContainerId = response.getId();

        this.dockerClient.copyArchiveToContainerCmd(dockerContainerId).withHostResource("/home/theccloud/template/" + task.template().name()).exec();

        MongoProvider.getINSTANCE().getTaskHandler().updateTask(task);

        this.dockerClient.startContainerCmd(dockerContainerId).exec();

        ServiceImpl service = new ServiceImpl(dockerContainerId, task.taskId(), serviceName);

        this.runningContainers.put(dockerContainerId, service);

        task.setRunningServices(task.getRunningServices() + 1);

        this.taskHandler.updateTask(task);

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
