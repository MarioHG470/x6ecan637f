package com.example;

import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;


public class HttpResponse {
    private String statusLine;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(String statusLine, Map<String, String> headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() { return statusLine; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }

    public JSONObject json() {
        return new JSONObject(body);
    }

    // 🚀 Safe accessors
    public String getJsonString(String path) {
        try {
            return navigate(path).getString(lastKey(path));
        } catch (Exception e) { return null; }
    }

    public Integer getJsonInt(String path) {
        try {
            return navigate(path).getInt(lastKey(path));
        } catch (Exception e) { return null; }
    }

    public Boolean getJsonBoolean(String path) {
        try {
            return navigate(path).getBoolean(lastKey(path));
        } catch (Exception e) { return null; }
    }

    // Helper methods
    private JSONObject navigate(String path) {
        String[] parts = path.split("\\.");
        JSONObject current = json();
        for (int i = 0; i < parts.length - 1; i++) {
            current = current.getJSONObject(parts[i]);
        }
        return current;
    }

    private String lastKey(String path) {
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }

    // 🚀 New helper: list keys at a path
    public List<String> listJsonKeys(String path) {
        List<String> keys = new ArrayList<>();
        try {
            JSONObject current = navigate(path);
            Iterator<String> it = current.keys();
            while (it.hasNext()) {
                keys.add(it.next());
            }
        } catch (Exception e) {
            // return empty list if path not found
        }
        return keys;
    }

    // 🚀 New helper: list keys and values at a path
    public Map<String, Object> listJsonEntries(String path) {
        Map<String, Object> entries = new LinkedHashMap<>();
        try {
            JSONObject current = navigate(path);
            Iterator<String> it = current.keys();
            while (it.hasNext()) {
                String key = it.next();
                Object value = current.get(key);
                entries.put(key, value);
            }
        } catch (Exception e) {
            // return empty map if path not found
        }
        return entries;
    }

    // 🚀 New helper: list array elements at a path
    public List<Object> listJsonArray(String path) {
        List<Object> elements = new ArrayList<>();
        try {
            String[] parts = path.split("\\.");
            JSONObject current = json();
            for (int i = 0; i < parts.length - 1; i++) {
                current = current.getJSONObject(parts[i]);
            }
            JSONArray arr = current.getJSONArray(parts[parts.length - 1]);
            for (int i = 0; i < arr.length(); i++) {
                elements.add(arr.get(i));
            }
        } catch (Exception e) {
            // return empty list if path not found or not an array
        }
        return elements;
    }

    public Object exploreJson(String path) {
        try {
            String[] parts = path.split("\\.");
            JSONObject current = json();
            for (int i = 0; i < parts.length - 1; i++) {
                current = current.getJSONObject(parts[i]);
            }
            String last = parts[parts.length - 1];

            Object target = current.get(last);

            if (target instanceof JSONObject) {
                Map<String, Object> entries = new LinkedHashMap<>();
                JSONObject obj = (JSONObject) target;
                Iterator<String> it = obj.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    entries.put(key, obj.get(key));
                }
                return entries;
            } else if (target instanceof JSONArray) {
                List<Object> elements = new ArrayList<>();
                JSONArray arr = (JSONArray) target;
                for (int i = 0; i < arr.length(); i++) {
                    elements.add(arr.get(i));
                }
                return elements;
            } else {
                return target; // primitive value
            }
        } catch (Exception e) {
            return null; // path not found
        }
    }

    // 🚀 Pretty-print JSON at a path
    public String prettyJson(String path) {
        try {
            String[] parts = path.split("\\.");
            JSONObject current = json();
            for (int i = 0; i < parts.length - 1; i++) {
                current = current.getJSONObject(parts[i]);
            }
            String last = parts[parts.length - 1];
            Object target = current.get(last);

            if (target instanceof JSONObject) {
                return ((JSONObject) target).toString(2); // indent 2 spaces
            } else if (target instanceof JSONArray) {
                return ((JSONArray) target).toString(2);
            } else {
                return String.valueOf(target); // primitive value
            }
        } catch (Exception e) {
            return "<not found>";
        }
    }

    // 🚀 Pretty-print the entire response body
    public String prettyBody() {
        try {
            Object parsed = new JSONObject(body);   // try as object
            return ((JSONObject) parsed).toString(2);
        } catch (Exception e1) {
            try {
                Object parsed = new JSONArray(body); // try as array
                return ((JSONArray) parsed).toString(2);
            } catch (Exception e2) {
                return body; // fallback: plain text
            }
        }
    }
}





/* public class HttpResponse {
    private String statusLine;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(String statusLine, Map<String, String> headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() { return statusLine; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }

    public JSONObject json() {
        return new JSONObject(body);
    }

    // 🚀 New safe accessor
    public String getJsonField(String path) {
        try {
            String[] parts = path.split("\\.");
            JSONObject current = json();
            for (int i = 0; i < parts.length - 1; i++) {
                current = current.getJSONObject(parts[i]);
            }
            return current.getString(parts[parts.length - 1]);
        } catch (Exception e) {
            return null; // return null if field not found
        }
    }
} */




/* public class HttpResponse {
    private String statusLine;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(String statusLine, Map<String, String> headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() { return statusLine; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }

    // 🚀 New convenience method
    public JSONObject json() {
        return new JSONObject(body);
    }
} */





/* public class HttpResponse {
    private final String statusLine;
    private final Map<String, String> headers;
    private final String body;

    public HttpResponse(String statusLine, Map<String, String> headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() { return statusLine; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }

    @Override
    public String toString() {
        return statusLine + "\n" + headers + "\n\n" + body;
    }
} */
