package de.containercloud.api;

import lombok.Getter;

@Getter
public enum MinecraftPlatform {

    PAPER("PAPER"),
    SPIGOT("SPIGOT"),
    FORGE("FORGE"),
    SPONGE("SPONGE"),
    FABRIC("FABRIC");

    private final String platformId;

    MinecraftPlatform(String platformId) {

        this.platformId = platformId;
    }
}
