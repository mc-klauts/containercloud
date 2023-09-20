package de.containercloud.websocket.services;

import com.google.gson.Gson;
import de.containercloud.wrapper.CloudWrapper;
import io.javalin.websocket.WsConfig;
import lombok.val;
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

            //val response = ctx.getUpgradeCtx$javalin().res();
            //response.addHeader("Upgrade", "websocket");
            //response.addHeader("Connection", "Connection");
            //response.addHeader("Sec-WebSocket-Accept", ctx.header("Sec-WebSocket-Key"));
            //response.setStatus(101);

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
}
