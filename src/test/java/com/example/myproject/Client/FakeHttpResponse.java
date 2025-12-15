package com.example.myproject.client;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.util.Optional;
import javax.net.ssl.SSLSession;

/**
 * Minimal fake HttpResponse for testing.
 */
public class FakeHttpResponse implements HttpResponse<String> {
    private final int statusCode;
    private final String body;

    public FakeHttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String body() {
        return body;
    }

    // --- Required methods with safe defaults ---
    @Override
    public HttpRequest request() { return null; }

    @Override
    public Optional<HttpResponse<String>> previousResponse() { return Optional.empty(); }

    @Override
    public HttpHeaders headers() { return HttpHeaders.of(java.util.Map.of(), (s1, s2) -> true); }

    @Override
    public URI uri() { return URI.create("http://localhost"); }

    @Override
    public HttpClient.Version version() {
    	return HttpClient.Version.HTTP_1_1;
    }

    @Override
    public Optional<SSLSession> sslSession() { return Optional.empty(); }
}




/* package com.example.myproject.client;

public class FakeHttpResponse implements java.net.http.HttpResponse<String> {
    private final int statusCode;
    private final String body;

    public FakeHttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public int statusCode() { return statusCode; }

    @Override
    public String body() { return body; }

    // Implement other methods with defaults or throw UnsupportedOperationException
    @Override public java.net.http.HttpRequest request() { return null; }
    @Override public java.net.http.HttpHeaders headers() { return java.net.http.HttpHeaders.of(java.util.Map.of(), (s1, s2) -> true); }
    @Override public java.net.http.HttpResponse.Version version() { return java.net.http.HttpResponse.Version.HTTP_1_1; }
    @Override public java.net.http.HttpResponse<String> previousResponse() { return null; }
    @Override public java.util.Optional<java.net.http.HttpResponse<String>> previousResponseOptional() { return java.util.Optional.empty(); }
    @Override public java.net.URI uri() { return null; }
    @Override public java.util.Optional<java.net.http.SSLSession> sslSession() { return java.util.Optional.empty(); }
} */
