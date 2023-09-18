package de.containercloud.api;

/**
 * version = Minecraft Version
 * type = Cloud Service Type
 * platform = Server Version i.e. Paper, Spigot, Forge
 * */
public record MinecraftVersion(String version, ServiceType type, MinecraftPlatform platform) {
}
