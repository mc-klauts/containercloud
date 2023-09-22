package de.containercloud.web.http.tasks;

import com.google.gson.Gson;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.task.TaskImpl;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class CreateTaskRequest implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        // TODO - change token
        if (!ctx.header("Bearer").equals("test")) {

            ctx.res().sendError(401, "Token is invalid!");

            return;
        }

        val task = new Gson().fromJson(ctx.body(), TaskImpl.class);

        val savedTask = MongoProvider.getINSTANCE().getTaskHandler().createTask(task);

        ctx.json(savedTask);

    }
}
