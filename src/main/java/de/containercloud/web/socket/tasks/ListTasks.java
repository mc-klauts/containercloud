package de.containercloud.web.socket.tasks;

import com.google.gson.Gson;
import de.containercloud.database.MongoProvider;
import de.containercloud.impl.task.TaskImpl;
import de.containercloud.web.TokenChecker;
import io.javalin.websocket.WsConfig;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTasks {

    private static final List<Session> SESSIONS = new ArrayList<>();

    public ListTasks(WsConfig cfg) {

        cfg.onConnect(ctx -> {

            if (!TokenChecker.isTokenValid(ctx.header("Bearer")))
                return;

            SESSIONS.add(ctx.session);

            ctx.send("Handshake-Accepted!");

        });

        cfg.onClose(wsCloseContext -> SESSIONS.remove(wsCloseContext.session));

        cfg.onMessage(ctx -> {
            if (!TokenChecker.isTokenValid(ctx.header("Bearer")))
                return;

            ctx.session.getRemote().sendString(new Gson().toJson(MongoProvider.getINSTANCE().getTaskHandler().tasks()));
        });

    }

    public static void broadCastTasks(List<TaskImpl> tasks) {
        SESSIONS.forEach(session -> {
            try {
                session.getRemote().sendString(new Gson().toJson(tasks));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
