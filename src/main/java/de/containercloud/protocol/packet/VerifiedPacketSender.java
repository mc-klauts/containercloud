package de.containercloud.protocol.packet;

import de.containercloud.api.security.Verification;

import java.net.InetSocketAddress;

public record VerifiedPacketSender(Verification verification, String containerId, InetSocketAddress address) {
}
