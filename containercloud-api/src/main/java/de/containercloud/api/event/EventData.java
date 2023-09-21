package de.containercloud.api.event;

import com.google.gson.Gson;

public record EventData(String data) {

    public static EventData serialize(Object obj) {
        return new EventData(new Gson().toJson(obj));
    }

    public static <T> Object deserialize(EventData data, Class<T> tClass) {
        return new Gson().fromJson(data.data, tClass);
    }
}
