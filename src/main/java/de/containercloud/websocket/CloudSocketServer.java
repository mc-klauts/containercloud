package de.containercloud.websocket;

import de.containercloud.websocket.services.ListServices;
import io.javalin.Javalin;
import lombok.val;

public class CloudSocketServer {

    private final Javalin javalin;

    public CloudSocketServer() {
        this.javalin = Javalin.create()
                .start(50000);

        this.javalin.ws("/services/list", ListServices::new);
        this.javalin.get("/services/list", ctx -> {
            val response = ctx.res();
            response.addHeader("Upgrade", "websocket");
            response.addHeader("Connection", "Connection");
            response.addHeader("Sec-WebSocket-Accept", ctx.header("Sec-WebSocket-Key"));
            response.setStatus(101);
            response.sendRedirect("/services/list");

        });

        this.javalin.get("/services/create", ctx -> {

            val request = ctx.req();

            val response = ctx.res();
            if (request.getMethod().equals("POST")) {
                response.sendError(405, "Body is required for request!");
                return;
            }

            

        });

    }
}
