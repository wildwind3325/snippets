package com.hv.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {

    static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String str, Class<T> cls) {
        return gson.fromJson(str, cls);
    }
}
