package de.containercloud.web.socket.services;

import com.google.gson.Gson;
import de.containercloud.api.service.Service;
import de.containercloud.web.TokenChecker;
import de.containercloud.wrapper.CloudWrapper;
import io.javalin.websocket.WsConfig;
import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

public class ListServices {

    private static final List<Session> SESSIONS = new ArrayList<>();

    public ListServices(WsConfig wsConfig) {

        wsConfig.onConnect(ctx -> {

            if (!TokenChecker.isTokenValid(ctx.header("Bearer")))
                return;

            SESSIONS.add(ctx.session);

            ctx.session.getRemote().sendString("Handshake-Accepted!");

        });

        wsConfig.onClose(wsCloseContext -> SESSIONS.remove(wsCloseContext.session));

        wsConfig.onMessage(ctx -> {

            if (!TokenChecker.isTokenValid(ctx.header("Bearer")))
                return;

            ctx.session.getRemote().sendString(new Gson().toJson(CloudWrapper.getINSTANCE().getContainerWrapper().listRunningContainers()));

        });

    }

    @SneakyThrows
    public static void broadcastServiceUpdate(List<Service> services) {

        for (Session session : SESSIONS)
            session.getRemote().sendString(new Gson().toJson(services));

    }
}
