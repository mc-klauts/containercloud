package de.containercloud.web;

import de.containercloud.web.http.services.CreateServiceRequest;
import de.containercloud.web.http.tasks.CreateTaskRequest;
import de.containercloud.web.http.tasks.DeleteTaskRequest;
import de.containercloud.web.socket.services.ListServices;
import de.containercloud.web.socket.tasks.ListTasks;
import io.javalin.Javalin;

public class CloudSocketServer {

    private final Javalin javalin;

    public CloudSocketServer() {

        this.javalin = Javalin.create()
                .start(50000);

        // Websockets

        // Services
        this.javalin.ws("/services/list", ListServices::new);

        // Tasks
        this.javalin.ws("/tasks/list", ListTasks::new);


        // Http

        // Services
        this.javalin.post("/services/create", new CreateServiceRequest());
        this.javalin.post("/services/delete", new DeleteTaskRequest());

        // Tasks
        this.javalin.post("/tasks/create", new CreateTaskRequest());
        this.javalin.delete("/tasks/delete", new DeleteTaskRequest());

    }
}
