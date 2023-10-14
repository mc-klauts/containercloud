package de.containercloud.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.containercloud.velocity.packet.PacketHandler;
import org.slf4j.Logger;

@Plugin(id = "theccloud", authors = "DasShorty", version = "1.0.0")
public class VelocityPlugin {

    private final ProxyServer proxyServer;
    private final Logger logger;
    private final PacketHandler packetHandler;

    @Inject
    public VelocityPlugin(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;

        this.logger.info("TheCCloud Plugin is starting!");
        this.packetHandler = new PacketHandler(this.logger);

        this.packetHandler.sendVerification();
    }
}
