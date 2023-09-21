package de.containercloud.api.registry;

public interface CloudRegistry extends CloudService {

    <T extends CloudService> T getInstance(Class<T> clazz);

    //void addService(Class<? extends CloudService> clazz, CloudService service);


}
