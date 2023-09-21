package de.containercloud.registry;

import de.containercloud.api.registry.CloudRegistry;
import de.containercloud.api.registry.CloudService;

import java.util.HashMap;
import java.util.Map;

public class CloudRegistryImpl implements CloudRegistry {

    private final Map<Class<?>, Object> servicePoint = new HashMap<>();


    @Override
    public <T extends CloudService> T getInstance(Class<T> clazz) {
        if (!this.servicePoint.containsKey(clazz))
            return null;

        return ((T) this.servicePoint.get(clazz));
    }

    public void addService(Class<? extends CloudService> clazz, CloudService service) {
        this.servicePoint.put(clazz, service);
    }


}
