package com.example;

import org.json.JSONObject;

public class JsonHelper {
    public static JSONObject parseBody(HttpResponse response) {
        return new JSONObject(response.getBody());
    }
}
