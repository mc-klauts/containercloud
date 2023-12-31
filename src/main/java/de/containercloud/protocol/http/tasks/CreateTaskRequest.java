package de.containercloud.protocol.http.tasks;

import com.google.gson.Gson;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.task.TaskImpl;
import de.containercloud.protocol.TokenChecker;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class CreateTaskRequest implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        if (!TokenChecker.isTokenValid(ctx.header("Bearer"))) {

            ctx.res().sendError(401, "Token is invalid!");

            return;
        }

        TaskImpl task = new Gson().fromJson(ctx.body(), TaskImpl.class);

        boolean savedTask = MongoProvider.getINSTANCE().getTaskHandler().createTask(task);

        ctx.json(savedTask);

    }
}
