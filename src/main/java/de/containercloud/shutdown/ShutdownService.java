package de.containercloud.shutdown;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ShutdownService {

    private static final Map<Integer, List<Consumer<?>>> CLOSE_LIST = new HashMap<>();

    public ShutdownService() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Integer priority : CLOSE_LIST.keySet()) {

                if (!CLOSE_LIST.containsKey(priority))
                    continue;

                List<Consumer<?>> consumers = CLOSE_LIST.get(priority);

                consumers.forEach(consumer -> consumer.accept(null));
            }
        }));
    }

    public static void addShutdown(int priority, Consumer<?> closeable) {

        if (!CLOSE_LIST.containsKey(priority))
            CLOSE_LIST.put(priority, new ArrayList<>());

        List<Consumer<?>> consumers = CLOSE_LIST.get(priority);
        consumers.add(closeable);
        CLOSE_LIST.put(priority, consumers);
    }
}
