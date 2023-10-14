package de.containercloud.api;

;

public enum MinecraftPlatform {

    PAPER("PAPER"),
    VELOCITY("VELOCITY");

    private final String platformId;

    MinecraftPlatform(String platformId) {

        this.platformId = platformId;
    }

    public String getPlatformId() {
        return platformId;
    }

    @Override
    public String toString() {
        return platformId;
    }
}
