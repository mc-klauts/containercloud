package de.containercloud.protocol.http.services;

import com.google.gson.Gson;
import de.containercloud.database.MongoProvider;
import de.containercloud.protocol.http.body.CreateServiceBody;
import de.containercloud.wrapper.CloudWrapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateServiceRequest implements Handler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        if (!ctx.header("Bearer").equals("test")) {

            ctx.res().sendError(401, "Token is invalid!");

            return;
        }

        val body = new Gson().fromJson(ctx.body(), CreateServiceBody.class);

        if (!MongoProvider.getINSTANCE().getTaskHandler().existTask(body.taskId())) {
            logger.warn("TaskId can't be found in request");
            ctx.res().sendError(404, "TaskId can't be found!");
            return;
        }

        val task = MongoProvider.getINSTANCE().getTaskHandler().task(body.taskId());

        val service = CloudWrapper.getINSTANCE().getContainerWrapper().runService(task);

        ctx.json(service);
    }
}
