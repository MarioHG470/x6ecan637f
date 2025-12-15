package com.example.myproject;

import com.example.myproject.client.HttpClientUtil;
import com.example.myproject.client.HttpClientWrapper;
import com.example.myproject.client.FakeHttpResponse;
import com.example.myproject.exception.ApiException;
import com.example.myproject.model.ErrorResponse;
import com.example.myproject.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HttpClientUtilTest {

    @Test
    void testGetSuccess() throws Exception {
        // Mock the wrapper and simulate a successful 200 OK response
        HttpClientWrapper mockWrapper = Mockito.mock(HttpClientWrapper.class);
        HttpResponse<String> fakeResponse =
                new FakeHttpResponse(200, "{\"id\":1,\"name\":\"Test User\"}");

        when(mockWrapper.send(any(HttpRequest.class))).thenReturn(fakeResponse);

        HttpClientUtil client = new HttpClientUtil(mockWrapper, new ObjectMapper());

        User user = client.get("https://jsonplaceholder.typicode.com/users/1", User.class);

        // Verify the parsed user object
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Test User", user.getName());

        // Extra check: ensure the fake response itself is correct
        assertEquals(200, fakeResponse.statusCode());
        assertEquals("{\"id\":1,\"name\":\"Test User\"}", fakeResponse.body());
    }

    @Test
    void testGetErrorHandling() throws Exception {
        // Mock the wrapper and simulate a 404 Not Found response
        HttpClientWrapper mockWrapper = Mockito.mock(HttpClientWrapper.class);
        HttpResponse<String> fakeResponse =
                new FakeHttpResponse(404, "{\"status\":404,\"message\":\"Not Found\"}");

        when(mockWrapper.send(any(HttpRequest.class))).thenReturn(fakeResponse);

        HttpClientUtil client = new HttpClientUtil(mockWrapper, new ObjectMapper());

        // Expect an ApiException when calling an invalid endpoint
        ApiException ex = assertThrows(ApiException.class, () -> {
            client.get("https://jsonplaceholder.typicode.com/invalid-endpoint", User.class);
        });

        // Verify the error object inside the exception
        ErrorResponse error = ex.getError();
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getMessage());

        // Extra check: ensure the fake response itself is correct
        assertEquals(404, fakeResponse.statusCode());
        assertEquals("{\"status\":404,\"message\":\"Not Found\"}", fakeResponse.body());
    }

    @Test
    void testGetServerErrorHandling() throws Exception {
    	// Mock the wrapper and simulate a 500 Internal Server Error response
    	HttpClientWrapper mockWrapper = Mockito.mock(HttpClientWrapper.class);
    	HttpResponse<String> fakeResponse =
            new FakeHttpResponse(500, "{\"status\":500,\"message\":\"Internal Server Error\"}");

    	when(mockWrapper.send(any(HttpRequest.class))).thenReturn(fakeResponse);

    	HttpClientUtil client = new HttpClientUtil(mockWrapper, new ObjectMapper());

    	ApiException ex = assertThrows(ApiException.class, () -> {
        	client.get("https://jsonplaceholder.typicode.com/server-error", User.class);
    	});

    	ErrorResponse error = ex.getError();
    	assertNotNull(error);
    	assertEquals(500, error.getStatus());
    	assertEquals("Internal Server Error", error.getMessage());
    }
}





/* package com.example.myproject;

import com.example.myproject.client.HttpClientUtil;
import com.example.myproject.exception.ApiException;
import com.example.myproject.model.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpClientUtilTest {

    @Test
    void testGetSuccess() throws Exception {
        HttpClientUtil client = new HttpClientUtil();
        var user = client.get("https://jsonplaceholder.typicode.com/users/1",
                              com.example.myproject.model.User.class);

        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    void testGetErrorHandling() {
        HttpClientUtil client = new HttpClientUtil();
        Exception ex = assertThrows(ApiException.class, () -> {
            client.get("https://jsonplaceholder.typicode.com/invalid-endpoint",
                       com.example.myproject.model.User.class);
        });

        ApiException apiEx = (ApiException) ex;
        ErrorResponse error = apiEx.getError();
        assertNotNull(error);
        assertTrue(error.getStatus() >= 400);
    }
} */