package com.example.myproject.client;

import com.example.myproject.exception.ApiException;
import com.example.myproject.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;

/**
 * Utility class for making HTTP requests with error handling.
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private final HttpClientWrapper client;
    private final ObjectMapper mapper;

    // Default constructor uses the real HttpClient
    public HttpClientUtil() {
        this(new DefaultHttpClientWrapper(), new ObjectMapper());
    }

    // Injectable constructor for testing/mocking
    public HttpClientUtil(HttpClientWrapper client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    // Generic GET
    public <T> T get(String url, Class<T> responseType) throws ApiException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return send(request, responseType);
    }

    // Generic POST
    public <T> T post(String url, Object body, Class<T> responseType) throws ApiException {
        try {
            String json = mapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            return send(request, responseType);
        } catch (IOException e) {
            ErrorResponse error = new ErrorResponse();
            error.setStatus(500); // serialization failure
            error.setMessage("Failed to serialize request body: " + e.getMessage());
            error.setPath(url);
            throw new ApiException(error);
        }
    }

    // Core send method with improved error handling
    private <T> T send(HttpRequest request, Class<T> responseType) throws ApiException {
        logger.info("{} {}", request.method(), request.uri());

        HttpResponse<String> response;
        try {
            // HttpClientWrapper declares "throws Exception", so we must catch it here
            response = client.send(request);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setStatus(500);
            error.setMessage("Transport error: " + e.getMessage());
            error.setPath(request.uri().toString());
            logger.error("Transport error: {}", e.getMessage());
            throw new ApiException(error);
        }

        int status = response.statusCode();

        if (status >= 400) {
            ErrorResponse error;
            try {
                // Try to parse JSON body into ErrorResponse
                error = mapper.readValue(response.body(), ErrorResponse.class);
            } catch (IOException parseEx) {
                // Fallback if body is not valid JSON
                error = new ErrorResponse();
                error.setMessage(response.body());
            }

            // Ensure all fields are set
            error.setStatus(status);
            if (error.getMessage() == null || error.getMessage().isBlank()) {
                error.setMessage("HTTP error " + status);
            }
            if (error.getPath() == null) {
                error.setPath(request.uri().toString());
            }

            logger.error("Error {}: {}", error.getStatus(), error.getMessage());
            throw new ApiException(error);
        }

        // Success case
        try {
            if (responseType.equals(String.class)) {
                return responseType.cast(response.body());
            }
            return mapper.readValue(response.body(), responseType);
        } catch (IOException e) {
            ErrorResponse error = new ErrorResponse();
            error.setStatus(500);
            error.setMessage("Failed to parse response body: " + e.getMessage());
            error.setPath(request.uri().toString());
            throw new ApiException(error);
        }
    }
}




/* package com.example.myproject.client;

import com.example.myproject.exception.ApiException;
import com.example.myproject.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private final HttpClient client;
    private final ObjectMapper mapper;

    public HttpClientUtil() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    // Generic GET
    public <T> T get(String url, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return send(request, responseType);
    }

    // Generic POST
    public <T> T post(String url, Object body, Class<T> responseType) throws Exception {
        String json = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(request, responseType);
    }

    // Core send method with improved error handling
    // Core send method with improved error handling
private <T> T send(HttpRequest request, Class<T> responseType) throws Exception {
    logger.info("{} {}", request.method(), request.uri());

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() >= 400) {
        ErrorResponse error;
        try {
            error = mapper.readValue(response.body(), ErrorResponse.class);
            if (error.getStatus() == 0) error.setStatus(response.statusCode());
            if (error.getMessage() == null || error.getMessage().isBlank()) {
                error.setMessage("HTTP error " + response.statusCode());
            }
            if (error.getPath() == null) error.setPath(request.uri().toString());
        } catch (Exception e) {
            error = new ErrorResponse();
            error.setStatus(response.statusCode());
            error.setMessage("HTTP error " + response.statusCode());
            error.setPath(request.uri().toString());
        }
        logger.error("Error {}: {}", error.getStatus(), error.getMessage());
        throw new ApiException(error);
    }

    // Special case: if caller wants raw JSON string, just return body
    if (responseType.equals(String.class)) {
        return responseType.cast(response.body());
    }

    return mapper.readValue(response.body(), responseType);
 }
} */




/* private <T> T send(HttpRequest request, Class<T> responseType) throws Exception {
    logger.info("{} {}", request.method(), request.uri());

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() >= 400) {
        ErrorResponse error;
        try {
            // Try to parse JSON error body
            error = mapper.readValue(response.body(), ErrorResponse.class);

            // If deserialized object has no status, set it explicitly
            if (error.getStatus() == 0) {
                error.setStatus(response.statusCode());
            }
            if (error.getMessage() == null || error.getMessage().isBlank()) {
                error.setMessage("HTTP error " + response.statusCode());
            }
            if (error.getPath() == null) {
                error.setPath(request.uri().toString());
            }

        } catch (Exception e) {
            // Fallback if body is not JSON
            error = new ErrorResponse();
            error.setStatus(response.statusCode());
            error.setMessage("HTTP error " + response.statusCode());
            error.setPath(request.uri().toString());
        }
        logger.error("Error {}: {}", error.getStatus(), error.getMessage());
        throw new ApiException(error);
    }

    return mapper.readValue(response.body(), responseType);
 }
} */