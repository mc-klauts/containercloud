package de.containercloud.api.registry;

import java.util.HashMap;
import java.util.Map;

public class CloudRegistry {

    private final Map<Class<?>, Object> servicePoint = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends CloudService> T getInstance(Class<T> clazz) {
        if (!this.servicePoint.containsKey(clazz))
            return null;

        return ((T) this.servicePoint.get(clazz));
    }

    public void addService(CloudService service) {
        this.servicePoint.put(service.getClass(), service);
    }


}
