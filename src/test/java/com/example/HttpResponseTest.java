package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.List;

public class HttpResponseTest {

    @Test
    public void testGetResponseArgs() {
        HttpResponse response = new HttpResponse(
            "HTTP/1.1 200 OK",
            Map.of("Content-Type", "application/json"),
            "{ \"args\": { \"foo\": \"bar\", \"baz\": \"qux\" } }"
        );

        assertEquals("bar", response.getJsonString("args.foo"));
        assertEquals("qux", response.getJsonString("args.baz"));
        assertEquals(Map.of("foo", "bar", "baz", "qux"), response.exploreJson("args"));
    }

    @Test
    public void testPostJsonResponse() {
        HttpResponse response = new HttpResponse(
            "HTTP/1.1 200 OK",
            Map.of("Content-Type", "application/json"),
            "{ \"json\": { \"username\": \"alice\", \"password\": \"secret123\" } }"
        );

        assertEquals("alice", response.getJsonString("json.username"));
        assertEquals("secret123", response.getJsonString("json.password"));
    }

    @Test
    public void testPutResponse() {
        HttpResponse response = new HttpResponse(
            "HTTP/1.1 200 OK",
            Map.of("Content-Type", "application/json"),
            "{ \"json\": { \"status\": \"active\", \"update\": \"new value\" } }"
        );

        assertEquals("active", response.getJsonString("json.status"));
        assertEquals("new value", response.getJsonString("json.update"));
    }

    @Test
    public void testArrayResponse() {
        HttpResponse response = new HttpResponse(
            "HTTP/1.1 200 OK",
            Map.of("Content-Type", "application/json"),
            "{ \"items\": [\"apple\", \"banana\", \"cherry\"], \"numbers\": [1,2,3,4] }"
        );

        List<Object> items = response.listJsonArray("items");
        assertEquals(List.of("apple", "banana", "cherry"), items);

        List<Object> numbers = response.listJsonArray("numbers");
        assertEquals(List.of(1, 2, 3, 4), numbers);
    }
}
