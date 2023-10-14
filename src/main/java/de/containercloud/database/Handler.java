package de.containercloud.database;

import com.google.gson.Gson;

public class Handler {

    private final Gson gson = new Gson();

    public Gson getGson() {
        return gson;
    }
}
