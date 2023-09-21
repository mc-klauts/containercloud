package de.containercloud.websocket.services;

import com.google.gson.Gson;
import de.containercloud.api.service.Service;
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

            // TODO - change token
            if (!ctx.header("Bearer").equals("test"))
                return;

            SESSIONS.add(ctx.session);

            ctx.session.getRemote().sendString("Handshake-Accepted!");

        });

        wsConfig.onClose(wsCloseContext -> {
            SESSIONS.remove(wsCloseContext.session);
        });

        wsConfig.onMessage(wsMessageContext -> {

            // TODO - change token
            if (!wsMessageContext.header("Bearer").equals("test"))
                return;

            wsMessageContext.session.getRemote().sendString(new Gson().toJson(CloudWrapper.getINSTANCE().getContainerWrapper().listRunningContainers()));

        });

    }

    @SneakyThrows
    public static void broadcastServiceUpdate(List<Service> services) {

        for (Session session : SESSIONS)
            session.getRemote().sendString(new Gson().toJson(services));

    }
}
