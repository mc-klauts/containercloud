package de.containercloud.api;

import de.containercloud.api.registry.CloudRegistry;
import lombok.Getter;

@Getter
public class CloudAPI {

    @Getter
    private static CloudAPI CLOUD_API;
    private final CloudRegistry cloudRegistry;

    public CloudAPI(CloudRegistry registry) {
        CLOUD_API = this;
        this.cloudRegistry = registry;
    }
}
