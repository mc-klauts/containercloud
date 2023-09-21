package de.containercloud.websocket;

import de.containercloud.websocket.services.ListServices;
import io.javalin.Javalin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CloudSocketServer {

    private final Javalin javalin;

    public CloudSocketServer() {
        this.javalin = Javalin.create()
                .start(50000);

        this.javalin.ws("/services/list", ListServices::new);

    }
}
