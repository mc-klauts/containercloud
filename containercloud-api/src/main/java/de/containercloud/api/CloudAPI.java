package de.containercloud.api;

import de.containercloud.api.registry.CloudRegistry;
import lombok.Getter;

@Getter
public class CloudAPI {

    private static CloudAPI CLOUD_API;
    private final CloudRegistry cloudRegistry;

    public CloudAPI() {
        CLOUD_API = this;
        this.cloudRegistry = new CloudRegistry();
    }
}
