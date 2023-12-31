package de.containercloud.protocol.http.services;

import de.containercloud.protocol.TokenChecker;
import de.containercloud.wrapper.CloudWrapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class GetServiceRequest implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        if (!TokenChecker.isTokenValid(ctx.header("Bearer"))) {

            ctx.res().sendError(401, "Token is invalid!");

            return;
        }

        String serviceId = ctx.header("serviceId");

        ctx.json(CloudWrapper.getINSTANCE().getServiceManager().withId(serviceId));

    }
}
