package de.containercloud.api.packets;
// This Interface represents a Packet
public interface Packet<PACKET extends Packet> {
    String serialize();

   PACKET deserialize();
}
