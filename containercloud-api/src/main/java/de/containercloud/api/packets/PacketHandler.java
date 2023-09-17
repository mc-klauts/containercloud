package de.containercloud.api.packets;

@FunctionalInterface
public interface PacketHandler {
    boolean handle(Packet packet);
}
