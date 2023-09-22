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
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.service.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CloudContainerWrapper {

    private final DockerClient dockerClient;
    private final Map<String, Service> runningContainers = new HashMap<>();
    private final MongoDatabaseHandler databaseHandler;

    public List<String> listRunningContainers() {
        return new ArrayList<>(runningContainers.keySet());
    }

    private String createServiceName(Task task) {

        val collection = EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.SERVICE);

        this.databaseHandler.collection(collection).find(Filters.eq(""));

        return "";
    }

    public ServiceImpl runService(Task task) {

        val serviceName = createServiceName(task);

        val createContainer = this.dockerClient.createContainerCmd("itzg/minecraft-server:latest")
                .withName("cloud-" + serviceName)
                .withEnv("TYPE=" + task.configuration().version().platform().getPlatformId(),
                        "EULA=true");

        createContainer
                .getHostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPortRange(50000, 60000), ExposedPort.tcp(25565)));

        val response = createContainer.withVolumes()
                .exec();

        val id = response.getId();

        val service = new ServiceImpl(id, task.taskId(), serviceName);

        this.runningContainers.put(id, service);

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
