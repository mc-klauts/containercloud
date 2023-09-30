package de.containercloud.wrapper;

import com.github.dockerjava.api.DockerClient;
import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.service.ServiceImpl;
import de.containercloud.impl.service.ServiceVolumeImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.Document;

@RequiredArgsConstructor
public class CloudVolumeWrapper {

    private final DockerClient dockerClient;
    private final MongoDatabaseHandler databaseHandler;


    public ServiceVolumeImpl createVolume(ServiceImpl service) {

        val collection = EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.VOLUME);

        val volumeName = service.serviceName() + "-data";
        val serviceVolume = new ServiceVolumeImpl(service.serviceId(), volumeName);

        val gson = new Gson();

        this.databaseHandler.collection(collection).insertOne(gson.fromJson(gson.toJson(serviceVolume), Document.class));

        this.dockerClient.createVolumeCmd().withName(volumeName).exec();

        return serviceVolume;
    }

    public ServiceVolumeImpl volume(String volumeId) {

        val collection = EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.VOLUME);
        return new Gson().fromJson(this.databaseHandler.collection(collection).find(Filters.eq("volumeName", volumeId)).first().toJson(), ServiceVolumeImpl.class);

    }

}
