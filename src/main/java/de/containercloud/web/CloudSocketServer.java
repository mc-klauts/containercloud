package de.containercloud.web;

import de.containercloud.web.http.services.CreateServiceRequest;
import de.containercloud.web.http.tasks.CreateTaskRequest;
import de.containercloud.web.socket.services.ListServices;
import io.javalin.Javalin;

public class CloudSocketServer {

    private final Javalin javalin;

    public CloudSocketServer() {
        this.javalin = Javalin.create()
                .start(50000);

        this.javalin.ws("/services/list", ListServices::new);
        this.javalin.post("/services/create", new CreateServiceRequest());
        this.javalin.post("/tasks/create", new CreateTaskRequest());

    }
}
