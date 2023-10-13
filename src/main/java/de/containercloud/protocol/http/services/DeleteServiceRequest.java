package de.containercloud.protocol.http.services;

import de.containercloud.protocol.TokenChecker;
import de.containercloud.wrapper.CloudWrapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class DeleteServiceRequest implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        if (!TokenChecker.isTokenValid(ctx.header("Bearer"))) {

            ctx.res().sendError(401, "Token is invalid!");

            return;
        }

        val serviceId = ctx.header("serviceId");

        if (CloudWrapper.getINSTANCE().getServiceManager().removeService(serviceId))
            ctx.json("Service deleted!");
        else
            ctx.res().sendError(404, "Service can't be found!");

    }
}
