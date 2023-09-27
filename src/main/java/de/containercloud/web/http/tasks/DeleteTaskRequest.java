package de.containercloud.web.http.tasks;

import de.containercloud.database.MongoProvider;
import de.containercloud.web.TokenChecker;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class DeleteTaskRequest implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        if (!TokenChecker.isTokenValid(ctx.header("Bearer"))) {

            ctx.res().sendError(401, "Token is invalid!");

            return;
        }

        if (MongoProvider.getINSTANCE().getTaskHandler().deleteTask(ctx.header("taskId")))
            ctx.json("Task deleted");
        else
            ctx.res().sendError(404, "Task can't be deleted (Not found)");

    }
}
