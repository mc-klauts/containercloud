package de.containercloud.api;

import com.google.gson.Gson;

/**
 * @param version Minecraft Version
 * @param type  Cloud Service Type
 * @param platform Server Version i.e. Paper, Spigot, Forge
 * @see de.containercloud.api.service.configuration.ServiceConfiguration how to get MinecraftVersion
 * */
public record MinecraftVersion(String version, ServiceType type, MinecraftPlatform platform) {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
