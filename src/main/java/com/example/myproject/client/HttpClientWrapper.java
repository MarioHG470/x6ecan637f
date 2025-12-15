package com.example.myproject.client;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Simple wrapper interface around HttpClient to make it mockable in tests.
 */
public interface HttpClientWrapper {
    HttpResponse<String> send(HttpRequest request) throws Exception;
}
