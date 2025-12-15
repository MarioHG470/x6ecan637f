package com.example.myproject.client;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Default implementation that delegates to the real java.net.http.HttpClient.
 */
public class DefaultHttpClientWrapper implements HttpClientWrapper {
    private final java.net.http.HttpClient delegate = java.net.http.HttpClient.newHttpClient();

    @Override
    public HttpResponse<String> send(HttpRequest request) throws Exception {
        return delegate.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
    }
}
