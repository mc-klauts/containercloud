package de.containercloud.protocol.packet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.containercloud.api.event.EventType;
import de.containercloud.api.event.GsonData;
import de.containercloud.api.packets.Packet;
import de.containercloud.api.packets.PacketType;
import de.containercloud.api.packets.response.ErrorResponse;
import de.containercloud.api.packets.response.SuccessResponse;
import de.containercloud.api.security.Verification;
import de.containercloud.database.handler.MongoVerificationHandler;
import de.containercloud.impl.event.EventManagerImpl;
import lombok.val;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public class PacketHandler {
    private final Server server;
    private final EventManagerImpl eventManager;
    private final MongoVerificationHandler verificationHandler;

    private final List<Verification> verifications = new ArrayList<>();

    public PacketHandler(EventManagerImpl eventManager, MongoVerificationHandler verificationHandler) throws IOException {
        this.eventManager = eventManager;
        this.verificationHandler = verificationHandler;

        this.server = new Server();

        val kryo = server.getKryo();

        kryo.register(Packet.class);
        kryo.register(HashMap.class);
        kryo.register(SuccessResponse.class);
        kryo.register(ErrorResponse.class);

        addListener();

        server.start();

        server.bind(50001, 50002);
    }

    private void addListener() {
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println(connection.getRemoteAddressTCP()); // TODO - replace logging?
            }

            @Override
            public void received(Connection connection, Object object) {

                if (!(object instanceof Packet packet))
                    return;


                switch (packet.getPacketType()) {
                    case CUSTOM -> {

                        // check if the custom packet type is a verification packet
                        if (!packet.getPacketData().containsKey("AUTHORIZATION")) {

                            eventManager.callEvent(EventType.CUSTOM, GsonData.serialize(packet.getPacketData()));

                            connection.sendUDP(new SuccessResponse("Packet has been successfully handled!"));

                            return;
                        }

                        // verify docker container

                        if (!(packet.getPacketData().get("AUTHORIZATION") instanceof Verification verification)) {

                            connection.sendUDP(new ErrorResponse("Verification failed!"));

                            return;
                        }

                        if (verifications.contains(verification)) {

                            val responsePacket = new Packet(PacketType.CUSTOM);

                            responsePacket.addData("AUTHORIZATION", verification);
                            responsePacket.addData("CODE", 2); // already verified

                            return;
                        }

                        val responsePacket = new Packet(PacketType.CUSTOM);

                        responsePacket.addData("AUTHORIZATION", verification);
                        responsePacket.addData("CODE", 1); // verified

                        verifications.add(verification);

                        connection.sendTCP(responsePacket);
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {
                // TODO - maybe logging disconnect?
            }
        });
    }
}