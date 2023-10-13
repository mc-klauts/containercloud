package de.containercloud.protocol;

import de.containercloud.protocol.http.services.CreateServiceRequest;
import de.containercloud.protocol.http.services.DeleteServiceRequest;
import de.containercloud.protocol.http.services.GetServiceRequest;
import de.containercloud.protocol.http.tasks.CreateTaskRequest;
import de.containercloud.protocol.http.tasks.DeleteTaskRequest;
import de.containercloud.protocol.socket.services.ListServices;
import de.containercloud.protocol.socket.tasks.ListTasks;
import io.javalin.Javalin;

public class CloudSocketServer {

    private final Javalin javalin;

    public CloudSocketServer() {

        this.javalin = Javalin.create()
                .start(50000);

        // Websockets

        // Services
        this.javalin.ws("/list/services", ListServices::new);

        // Tasks
        this.javalin.ws("/list/tasks", ListTasks::new);


        // Http

        // Services
        this.javalin.post("/create/service", new CreateServiceRequest());
        this.javalin.post("/delete/service", new DeleteServiceRequest());
        this.javalin.post("/get/service", new GetServiceRequest());

        // Tasks
        this.javalin.post("/create/task", new CreateTaskRequest());
        this.javalin.delete("/delete/task", new DeleteTaskRequest());

    }
}
