package de.containercloud.velocity.packet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.containercloud.api.packets.Packet;
import de.containercloud.api.packets.PacketType;
import de.containercloud.api.packets.response.ErrorResponse;
import de.containercloud.api.packets.response.SuccessResponse;
import de.containercloud.api.security.Verification;
import lombok.val;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class PacketHandler {

    private final Client client;
    private final Logger logger;

    private void addListener() {
        this.client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                logger.info("Connected with Container Cloud!");
            }

            @Override
            public void received(Connection connection, Object object) {

                if (!(object instanceof Packet packet))
                    return;

                if (packet.getPacketType() != PacketType.CUSTOM)
                    return;

                val code = (int) packet.getPacketData().get("CODE");

                switch (code) {
                    case 1 -> logger.info("Cloud accepted verification request! Server is synchronized!");
                    case 2 -> logger.warn("Already synchronized with cloud! You can ignore this message but ensure that the request is not sent more then once!");
                }

            }

            @Override
            public void disconnected(Connection connection) {
                logger.warn("Disconnected from Container Cloud!");
            }
        });
    }

    public PacketHandler(Logger logger) {
        this.logger = logger;
        this.client = new Client();

        val kryo = this.client.getKryo();

        kryo.register(Packet.class);
        kryo.register(HashMap.class);
        kryo.register(ErrorResponse.class);
        kryo.register(SuccessResponse.class);

        addListener();

        this.client.start();
        try {
            this.client.connect(5000, "localhost", 50001, 50002);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendVerification() {

        val packet = new Packet(PacketType.CUSTOM);
        val token = System.getenv("VALIDATE-TOKEN");

        packet.addData("AUTHORIZATION", new Verification(token));

        this.client.sendTCP(packet);
    }
}
