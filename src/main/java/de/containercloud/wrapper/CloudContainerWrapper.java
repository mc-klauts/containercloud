package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import de.containercloud.api.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CloudContainerWrapper {

    private final DockerClient dockerClient;
    private final Map<String, Service> runningContainers = new HashMap<>();

    public void runService(Service service) {
        val response = this.dockerClient.createContainerCmd("itzg/minecraft-server:latest")
                .withName("cloud-" + service.serviceName())
                .withEnv("TYPE=" + service.task().configuration().version().platform().getPlatformId(),
                        "EULA=true")
                .withVolumes()
                .exec();

        val id = response.getId();

        this.runningContainers.put(id, service);
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
