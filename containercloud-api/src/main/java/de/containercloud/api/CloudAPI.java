package de.containercloud.api;

import de.containercloud.api.registry.CloudRegistry;

public class CloudAPI {

    private static CloudAPI CLOUD_API;
    private final CloudRegistry cloudRegistry;

    public CloudAPI(CloudRegistry registry) {
        CLOUD_API = this;
        this.cloudRegistry = registry;
    }

    public static CloudAPI getCloudApi() {
        return CLOUD_API;
    }

    public CloudRegistry getCloudRegistry() {
        return cloudRegistry;
    }
}
