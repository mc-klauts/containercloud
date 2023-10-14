package de.containercloud.api;

import lombok.Getter;

@Getter
public enum MinecraftPlatform {

    PAPER("PAPER"),
    VELOCITY("VELOCITY");

    private final String platformId;

    MinecraftPlatform(String platformId) {

        this.platformId = platformId;
    }

    @Override
    public String toString() {
        return platformId;
    }
}
