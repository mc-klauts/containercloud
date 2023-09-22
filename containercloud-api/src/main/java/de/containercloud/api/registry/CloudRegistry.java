package de.containercloud.api.registry;

public interface CloudRegistry extends CloudService {

    <T extends CloudService> T getCloudService(Class<T> clazz);


}
