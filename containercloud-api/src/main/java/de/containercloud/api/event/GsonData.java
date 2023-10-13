package de.containercloud.api.event;

import com.google.gson.Gson;

public record GsonData(String data) {

    public static GsonData serialize(Object obj) {
        return new GsonData(new Gson().toJson(obj));
    }

    public static <T> Object deserialize(GsonData data, Class<T> tClass) {
        return new Gson().fromJson(data.data, tClass);
    }
}
