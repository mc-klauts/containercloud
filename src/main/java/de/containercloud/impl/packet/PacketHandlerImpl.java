package de.containercloud.impl.packet;

import de.containercloud.api.packets.Packet;
import de.containercloud.api.packets.PacketHandler;

public class PacketHandlerImpl implements PacketHandler {

    @Override
    public boolean handle(Packet packet) {
        return false;
    }
}
