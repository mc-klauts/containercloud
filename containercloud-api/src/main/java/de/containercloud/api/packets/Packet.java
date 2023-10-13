package de.containercloud.api.packets;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Packet {

    private final HashMap<String, Object> packetData = new HashMap<>();

    @Getter
    private PacketType packetType;

    public Packet(PacketType packetType) {
        this.packetType = packetType;
    }

    public void addData(String key, Object value) {
        this.packetData.put(key, value);
    }

    public Map<String, Object> getPacketData() {
        return packetData;
    }
}